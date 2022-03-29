package in.hocg.boot.javacv.autoconfiguration.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.gif.AnimatedGifEncoder;
import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_objdetect;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

/**
 * Created by hocgin on 2022/3/29
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class FeatureHelper {

    public Path pngToVideo(File dir) {
        File[] files = FileUtil.ls(dir.getAbsolutePath());
        return pngToVideo(List.of(files));
    }

    /**
     * 图片转视频
     *
     * @param files
     * @return
     */
    @SneakyThrows
    public Path pngToVideo(List<File> files) {
        Path file = Files.createTempFile("test", ".mp4");

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(file.toFile(), 1920, 1080, 1);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_FLV1);
        recorder.setFormat("flv");
        recorder.setFrameRate(25);
        recorder.setPixelFormat(0);
        recorder.start();

        OpenCVFrameConverter.ToIplImage convert = new OpenCVFrameConverter.ToIplImage();

        for (File tFile : files) {
            IplImage image = cvLoadImage(tFile.getAbsolutePath());
            recorder.record(convert.convert(image));
            opencv_core.cvReleaseImage(image);
        }
        recorder.stop();
        recorder.release();
        return file;
    }

    /**
     * 多个视频转单个
     *
     * @param files
     * @return
     */
    @SneakyThrows
    public Path mergeVideo(List<File> files) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        Path tempFile = Files.createTempFile("test", ".mp4");

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(files.get(0));
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toString(), grabber.getImageWidth(), grabber.getImageHeight(), 0);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        int bitrate = grabber.getVideoBitrate();
        if (bitrate == 0) {
            bitrate = grabber.getAudioBitrate();
        }
        recorder.setVideoBitrate(bitrate);
        recorder.start();

        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            recorder.record(frame);
        }
        for (File file : files) {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            while ((frame = frameGrabber.grabImage()) != null) {
                recorder.record(frame);
            }
            frameGrabber.close();
        }
        recorder.close();
        grabber.close();
        return tempFile;
    }

    public Path mergeVideo(File dir) {
        return mergeVideo(List.of(FileUtil.ls(dir.getAbsolutePath())));
    }

    /**
     * 合并音频
     *
     * @param files
     * @return
     */
    @SneakyThrows
    public Path mergeAudio(List<File> files) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        File firstFile = files.get(0);
        String suffix = FileUtil.getSuffix(firstFile);
        Path result = Files.createTempFile("test", "." + suffix);

        FFmpegFrameGrabber firstGrabber = new FFmpegFrameGrabber(firstFile);
        firstGrabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(result.toString(), firstGrabber.getAudioChannels());
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
        recorder.setFrameRate(firstGrabber.getFrameRate());
        recorder.start();

        Frame frame;
        while ((frame = firstGrabber.grabFrame()) != null) {
            recorder.record(frame);
        }
        for (File file : files) {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            while ((frame = frameGrabber.grabFrame()) != null) {
                recorder.record(frame);
            }
            frameGrabber.close();
        }
        recorder.close();
        firstGrabber.close();
        return result;
    }

    public Path mergeAudio(File dir) {
        return mergeAudio(List.of(FileUtil.ls(dir.getAbsolutePath())));
    }


    /**
     * 给视频添加音频
     *
     * @param video
     * @param audio
     * @return
     */
    @SneakyThrows
    public Path addAudio(File video, File audio) {
        FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(video);
        FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audio);
        videoGrabber.start();
        audioGrabber.start();

        Path tempFile = Files.createTempFile("test", ".mp4");

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toFile(), videoGrabber.getImageWidth(),
            videoGrabber.getImageHeight(), 1);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setInterleaved(true);
        recorder.start(videoGrabber.getFormatContext());

        long videoTime = videoGrabber.getLengthInTime();
        long audioTime = audioGrabber.getLengthInTime();

        AVPacket packet;
        while ((packet = videoGrabber.grabPacket()) != null) {
            recorder.recordPacket(packet);
        }

        Frame frame;
        // 视频大于音频
        if (videoTime > audioTime) {
            long audioPlayTime = 0;
            while ((frame = audioGrabber.grabSamples()) != null || audioPlayTime < videoTime) {
                if (frame == null) {
                    audioGrabber.restart();
                    frame = audioGrabber.grabSamples();
                    videoTime -= audioPlayTime;
                }
                recorder.record(frame);
                audioPlayTime = audioGrabber.getTimestamp();
                if (audioPlayTime >= videoTime) {
                    break;
                }
            }
        }
        // 音频大于视频
        else {
            while ((frame = audioGrabber.grabSamples()) != null) {
                recorder.record(frame);
                if (audioGrabber.getTimestamp() >= videoTime) {
                    break;
                }
            }
        }

        recorder.close();
        videoGrabber.close();
        audioGrabber.close();
        return tempFile;
    }


    /**
     * 获取 视频/音频 时长
     *
     * @param videoOrAudio
     * @return s
     */
    @SneakyThrows
    public long getDurationTime(File videoOrAudio) {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoOrAudio.getAbsolutePath());
        grabber.start();
        long second = grabber.getLengthInTime() / (1000 * 1000);
        grabber.stop();
        return second;
    }

    /**
     * 推送视频流或图片流
     *
     * @param videoFiles
     * @param rtmpUrl
     */
    @SneakyThrows
    public void videoToRtmp(List<File> videoFiles, String rtmpUrl) {
        if (CollUtil.isEmpty(videoFiles)) {
            return;
        }
        File firstVideo = videoFiles.get(0);

        Loader.load(opencv_objdetect.class);

        // 本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
        FrameGrabber firstGrabber = FrameGrabber.createDefault(firstVideo);
        firstGrabber.start();

        // 转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        // 抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        IplImage grabbedImage = converter.convert(firstGrabber.grab());
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        FrameRecorder recorder = FrameRecorder.createDefault(rtmpUrl, width, height);
        // avcodec.AV_CODEC_ID_H264，编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // 封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFormat("flv");
        recorder.setFrameRate(firstGrabber.getFrameRate());
        recorder.start(); // 开启录制器

        int idx = 0;
        long startTime = System.currentTimeMillis();
        long timestamp;
        Frame frame;
        while ((firstGrabber.getLengthInFrames() <= idx++) && (frame = firstGrabber.grabFrame()) != null) {
            timestamp = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(timestamp);
            recorder.record(frame);
            Thread.sleep(40);
        }
        firstGrabber.close();

        for (File file : videoFiles) {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            while ((frame = frameGrabber.grabFrame()) != null) {
                timestamp = 1000 * (System.currentTimeMillis() - startTime);
                recorder.setTimestamp(timestamp);
                recorder.record(frame);
                Thread.sleep(40);
            }
            frameGrabber.close();
        }

        recorder.stop();
        recorder.release();
        firstGrabber.stop();
    }


    /**
     * 截图
     *
     * @param videoFile
     * @param timestamp
     * @return
     */
    @SneakyThrows
    public Path snapshot(File videoFile, long timestamp) {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoFile);
        grabber.start();
        grabber.setTimestamp(timestamp);
        Frame frame = grabber.grabImage();
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        Path result = Files.createTempFile("snapshot", ".jpg");
        File output = result.toFile();

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, FileUtil.getSuffix(output), output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            grabber.stop();
        }
        return result;
    }

    @SneakyThrows
    public Path processing(File video, BiFunction<BufferedImage, Integer, BufferedImage> videoProcessor) {
        Path result = Files.createTempFile("processing", ".mp4");

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(result.toFile(), grabber.getImageWidth(), grabber.getImageHeight(), 1);
        // 视频相关配置，取原视频配置
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(grabber.getVideoCodec());
        recorder.setVideoBitrate(grabber.getVideoBitrate());

        // 音频相关配置，取原音频配置
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
        recorder.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame;
        int i = 0;
        while ((frame = grabber.grab()) != null) {
            // 从视频帧中获取图片
            if (frame.image != null) {
                if (Objects.nonNull(videoProcessor)) {
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    // 视频帧赋值，写入输出流
                    frame.image = converter.getFrame(videoProcessor.apply(bufferedImage, i++)).image;
                }
                recorder.record(frame);
            }

            // 音频帧写入输出流
            if (frame.samples != null) {
                recorder.record(frame);
            }
        }

        recorder.close();
        grabber.close();
        return result;
    }

    /**
     * 消音
     *
     * @param video
     * @return
     */
    @SneakyThrows
    public Path erasure(File video) {
        Path result = Files.createTempFile("erasure", ".mp4");

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(result.toFile(), grabber.getImageWidth(), grabber.getImageHeight(), 1);

        // 视频相关配置，取原视频配置
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoCodec(grabber.getVideoCodec());
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.start();

        Frame frame;
        while ((frame = grabber.grab()) != null) {
            // 从视频帧中获取图片
            if (frame.image != null) {
                recorder.record(frame);
            }
        }

        recorder.close();
        grabber.close();
        return result;
    }

    /**
     * 提取音频
     *
     * @param video
     * @return
     */
    @SneakyThrows
    public Path toAudio(File video) {
        Path result = Files.createTempFile("erasure", ".mp3");
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(result.toFile(), 1);

        // 音频相关配置，取原音频配置
        recorder.setFormat("mp3");
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
        recorder.start();

        Frame frame;
        while ((frame = grabber.grab()) != null) {

            // 音频帧写入输出流
            if (frame.samples != null) {
                recorder.record(frame);
            }
        }

        recorder.close();
        grabber.close();
        return result;
    }

    /**
     * 部分转gif
     *
     * @param video
     * @param start
     * @param end
     * @return
     */
    @SneakyThrows
    public Path toGif(File video, long start, long end) {
        Path result = Files.createTempFile("toGif", ".gif");

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.setTimestamp(start);
        grabber.start();

        AnimatedGifEncoder en = new AnimatedGifEncoder();
        en.setFrameRate(Convert.toFloat(grabber.getFrameRate()));
        en.start(result.toString());
        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            long nowTimestamp = grabber.getTimestamp();
            if (nowTimestamp > end) {
                return result;
            }
            BufferedImage bufferedImage = converter.getBufferedImage(frame);
            en.addFrame(bufferedImage);
        }
        en.finish();
        grabber.stop();
        return result;
    }

}
