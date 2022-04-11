package in.hocg.boot.javacv.autoconfiguration.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.gif.AnimatedGifEncoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import in.hocg.boot.utils.function.ConsumerThrow;
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
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
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

    /**
     * 图片转视频
     *
     * @param files
     * @return
     */
    @SneakyThrows
    public File pngToVideo(List<File> files, File output) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        BufferedImage first = ImageIO.read(files.get(0));
        int height = first.getHeight();
        int width = first.getWidth();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, 1);
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
        return output;
    }

    public File pngToVideo(File dir, File output) {
        File[] files = FileUtil.ls(dir.getAbsolutePath());
        return pngToVideo(List.of(files), output);
    }

    public File mergeVideo(List<File> files, File output) {
        return mergeVideo(files, output, 0, 0);
    }

    /**
     * 多个视频转单个视频
     *
     * @param files     文件
     * @param output    输出文件
     * @param passStart 跳过开始时间
     * @param passEnd   跳过结束时间
     * @return
     */
    @SneakyThrows
    public File mergeVideo(List<File> files, File output, long passStart, long passEnd) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        Path tempFile = output.toPath();
        File firstFile = files.get(0);

        FFmpegFrameGrabber firstGrabber = new FFmpegFrameGrabber(firstFile);
        firstGrabber.start();
        if (passStart > 0) {
            firstGrabber.setTimestamp(passStart, true);
        }
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toString(),
            firstGrabber.getImageWidth(), firstGrabber.getImageHeight(), firstGrabber.getAudioChannels());
        recorder.setVideoCodec(firstGrabber.getVideoCodec());
        recorder.setFormat("mp4");
        recorder.setFrameRate(firstGrabber.getFrameRate());
        recorder.setAudioCodec(firstGrabber.getAudioCodec());
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        int bitrate = firstGrabber.getVideoBitrate();
        if (bitrate == 0) {
            bitrate = firstGrabber.getAudioBitrate();
        }
        recorder.setVideoBitrate(bitrate);
        recorder.start();

        AtomicLong timestamp = new AtomicLong();
        ConsumerThrow<FFmpegFrameGrabber> appendRecord = (FFmpegFrameGrabber grabber) -> {
            long endTimestamp = grabber.getLengthInTime() - passEnd;
            Frame frame;
            if (passStart > 0) {
                grabber.setTimestamp(passStart);
            }
            while ((frame = grabber.grabFrame()) != null) {
                long grabberTimestamp = grabber.getTimestamp();
                if (grabberTimestamp > endTimestamp) {
                    grabber.setTimestamp(grabber.getLengthInTime(), true);
                    break;
                }
                recorder.setTimestamp(timestamp.get() + grabberTimestamp);
                recorder.record(frame);
            }
            timestamp.addAndGet(endTimestamp);
            grabber.close();
        };
        appendRecord.accept(firstGrabber);

        FFmpegFrameGrabber frameGrabber;
        for (File file : CollUtil.sub(files, 1, files.size())) {
            frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            appendRecord.accept(frameGrabber);
        }
        recorder.close();
        return output;
    }

    @SneakyThrows
    public File mergeVideoStyle2(List<File> files, File output, long passStart, long passEnd) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        Path tempFile = output.toPath();
        File firstFile = files.get(0);

        FFmpegFrameGrabber firstGrabber = new FFmpegFrameGrabber(firstFile);
        firstGrabber.start();
        int imageHeight = firstGrabber.getImageHeight();
        int imageWidth = firstGrabber.getImageWidth();
        Pair<BufferedImage, File> bgFiles = ImageUtils.getBlackBufferedImage(imageWidth, imageHeight);
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(tempFile.toString(),
            imageWidth, imageHeight, firstGrabber.getAudioChannels());
        recorder.setVideoCodec(firstGrabber.getVideoCodec());
        recorder.setFormat("mp4");
        recorder.setFrameRate(firstGrabber.getFrameRate());
        recorder.setAudioCodec(firstGrabber.getAudioCodec());
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        int bitrate = firstGrabber.getVideoBitrate();
        if (bitrate == 0) {
            bitrate = firstGrabber.getAudioBitrate();
        }
        recorder.setVideoBitrate(bitrate);
        recorder.start();

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage iplImage = cvLoadImage(bgFiles.getValue().getAbsolutePath());
        Buffer[] bgImage = converter.convert(iplImage).image;
        opencv_core.cvReleaseImage(iplImage);

        AtomicLong timestamp = new AtomicLong();
        ConsumerThrow<FFmpegFrameGrabber> appendRecord = (FFmpegFrameGrabber grabber) -> {
            long lengthInTime = grabber.getLengthInTime();
            long endTimestamp = lengthInTime - passEnd;
            Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                long grabberTimestamp = grabber.getTimestamp();
                if ((grabberTimestamp < passStart || grabberTimestamp > endTimestamp) && frame.image != null) {
                    frame.image = bgImage;
                    recorder.setTimestamp(timestamp.get() + grabberTimestamp);
                    recorder.record(frame);
                } else {
                    recorder.setTimestamp(timestamp.get() + grabberTimestamp);
                    recorder.record(frame);
                }
            }
            timestamp.addAndGet(lengthInTime);
            grabber.close();
        };

        appendRecord.accept(firstGrabber);

        FFmpegFrameGrabber frameGrabber;
        for (File file : CollUtil.sub(files, 1, files.size())) {
            frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            appendRecord.accept(frameGrabber);
        }
        recorder.close();
        return output;
    }

    public File mergeVideo(File dir, File output) {
        return mergeVideo(List.of(FileUtil.ls(dir.getAbsolutePath())), output);
    }

    /**
     * 合并音频
     *
     * @param files
     * @return
     */
    @SneakyThrows
    public File mergeAudio(List<File> files, File output) {
        if (CollUtil.isEmpty(files)) {
            return null;
        }
        File firstFile = files.get(0);

        FFmpegFrameGrabber firstGrabber = new FFmpegFrameGrabber(firstFile);
        firstGrabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, firstGrabber.getAudioChannels());
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
        recorder.setFrameRate(firstGrabber.getFrameRate());
        recorder.start();


        AtomicLong timestamp = new AtomicLong();
        ConsumerThrow<FFmpegFrameGrabber> appendRecord = (FFmpegFrameGrabber grabber) -> {
            long lengthInTime = grabber.getLengthInTime();
            Frame frame;
            long grabberTimestamp = grabber.getTimestamp();
            while ((frame = grabber.grabFrame()) != null) {
                recorder.setTimestamp(timestamp.get() + grabberTimestamp);
                recorder.record(frame);
            }
            timestamp.addAndGet(lengthInTime);
            grabber.close();
        };

        appendRecord.accept(firstGrabber);
        for (File file : CollUtil.sub(files, 1, files.size())) {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
            frameGrabber.start();
            appendRecord.accept(frameGrabber);
        }
        recorder.close();
        firstGrabber.close();
        return output;
    }

    public File mergeAudio(File dir, File output) {
        return mergeAudio(List.of(FileUtil.ls(dir.getAbsolutePath())), output);
    }


    /**
     * 给视频添加音频
     *
     * @param video
     * @param audio
     * @return
     */
    @SneakyThrows
    public File addAudio(File video, File audio, File output) {
        FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(video);
        FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audio);
        videoGrabber.start();
        audioGrabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, videoGrabber.getImageWidth(),
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
        return output;
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

        for (File file : CollUtil.sub(videoFiles, 1, videoFiles.size())) {
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
     * 视频截取
     *
     * @param video
     * @param start
     * @param end
     * @param output
     * @return
     */
    @SneakyThrows
    public File subVideo(String video, long start, long end, File output) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(video);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.setVideoTimestamp(start);
        grabber.setAudioTimestamp(start);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getImageWidth(), grabber.getImageHeight(),
            grabber.getAudioChannels());
        recorder.setVideoCodec(grabber.getVideoCodec());
        recorder.setFormat("mp4");
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setAudioCodec(grabber.getAudioCodec());
        recorder.setAudioBitrate(grabber.getAudioBitrate());
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.start();

        Frame frame;
        while ((frame = grabber.grabFrame()) != null) {
            if (grabber.getTimestamp() > end) {
                recorder.close();
                grabber.close();
                return output;
            }
            recorder.record(frame);
        }
        recorder.close();
        grabber.close();
        return output;
    }

    public File subVideo(File videoFile, long start, long end, File output) {
        return subVideo(videoFile.getAbsolutePath(), start, end, output);
    }


    /**
     * 截图
     *
     * @param videoFile
     * @param timestamp
     * @return
     */
    @SneakyThrows
    public File snapshot(File videoFile, long timestamp, File output) {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoFile);
        grabber.start();
        grabber.setVideoTimestamp(timestamp);
        grabber.setAudioTimestamp(timestamp);
        Frame frame = grabber.grabImage();
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, FileUtil.getSuffix(output), output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            grabber.stop();
        }
        return output;
    }

    @SneakyThrows
    public File processing(File video, BiFunction<BufferedImage, Integer, BufferedImage> videoProcessor, File output) {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getImageWidth(), grabber.getImageHeight(),
            grabber.getAudioChannels());
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
        return output;
    }

    /**
     * 消音
     *
     * @param video
     * @return
     */
    @SneakyThrows
    public File erasure(File video, File output) {

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getImageWidth(), grabber.getImageHeight(),
            grabber.getAudioChannels());

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
        return output;
    }

    /**
     * 提取音频
     *
     * @param video
     * @return
     */
    @SneakyThrows
    public File toAudio(File video, File output) {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, grabber.getAudioChannels());

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
        return output;
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
    public File toGif(File video, long start, long end, File output) {

        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(video);
        grabber.setVideoTimestamp(start);
        grabber.setAudioTimestamp(start);
        grabber.start();

        AnimatedGifEncoder en = new AnimatedGifEncoder();
        en.setFrameRate(Convert.toFloat(grabber.getFrameRate()));
        en.start(output.getAbsolutePath());
        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            long nowTimestamp = grabber.getTimestamp();
            if (nowTimestamp > end) {
                en.finish();
                grabber.stop();
                return output;
            }
            BufferedImage bufferedImage = converter.getBufferedImage(frame);
            en.addFrame(bufferedImage);
        }
        en.finish();
        grabber.stop();
        return output;
    }

    public static void main(String[] args) {


    }

}
