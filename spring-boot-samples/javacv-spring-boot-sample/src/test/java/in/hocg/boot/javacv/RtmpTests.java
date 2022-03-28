package in.hocg.boot.javacv;

import cn.hutool.core.io.FileUtil;
import in.hocg.boot.javacv.autoconfiguration.support.FeatureHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_objdetect;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hocgin on 2021/10/6
 * email: hocgin@gmail.com
 * - https://www.cnblogs.com/eguid/p/14268230.html
 * - https://github.com/opencv/opencv
 *
 * @author hocgin
 */
@Slf4j
public class RtmpTests {
    String testRtmpUrl = "rtmp://160107.livepush.myqcloud.com/live/sss?txSecret=7f8bf7b6dd1610c8b5602453291b2c1f&txTime=61DE4DAC";

    @Test
    public void testXX() {
    }

    @Test
    public void imageDataToRtmp() {
        Path path = FeatureHelper.png2Video(Paths.get("/Users/hocgin/Projects/spring-boot-starters-project/spring-boot-samples/javacv-spring-boot-sample/src/test/resources/image2video").toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void muteDataToRtmp() {
        //
    }

    @Test
    public void frameToRtmp() {

    }

    @Test
    @SneakyThrows
    public void imageObjToRtmp() {
        String[] files = {
            "/Users/hocgin/Desktop/图片收藏/logo/b830f86b040de4bc5ce27ad7b83335e4.png",
            "/Users/hocgin/Desktop/图片收藏/logo/b830f86b040de4bc5ce27ad7b83335e4.png",
            "/Users/hocgin/Desktop/图片收藏/logo/b830f86b040de4bc5ce27ad7b83335e4.png",
            "/Users/hocgin/Desktop/图片收藏/logo/9db4306a2b277f76c5f96e84cc60504e.png",
            "/Users/hocgin/Desktop/图片收藏/logo/9db4306a2b277f76c5f96e84cc60504e.png",
            "/Users/hocgin/Desktop/图片收藏/logo/9db4306a2b277f76c5f96e84cc60504e.png",
        };
        int videoLen = 60; // 60s
        int deMillis = 40; // 延迟
        int count = videoLen * 1000 / deMillis;
        double frameRate = 25D;

        int frameWidth = 480;
        int frameHeight = 270;

        FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault(testRtmpUrl, frameWidth, frameHeight);
        recorder.setInterleaved(true);
        // 降低编码延时
        recorder.setVideoOption("tune", "zerolatency");
        // 提升编码速度
        recorder.setVideoOption("preset", "ultrafast");
        // 视频质量参数(详见 https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("crf", "28");
        // 分辨率
        recorder.setVideoBitrate(2000000);
        recorder.setFrameRate(frameRate < 0 ? frameRate : 30);// 设置帧率
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.setAudioChannels(1);
        recorder.start();
        long startTime = System.currentTimeMillis();

        new CanvasFrame("test").setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        for (int i = 0; i < count; i++) {
            // BufferedImage image = ImageUtils.bytesToImage(ImageUtils.imageToBytes(file));
            BufferedImage image = ImageIO.read(FileUtil.file(files[i % files.length]));
//            recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));
            recorder.record(new Java2DFrameConverter().convert(image));
//            Thread.sleep(deMillis);
        }

        recorder.stop();
        recorder.release();
    }

    @Test
    public void rtmpToRtmp() {
        toRtmp("rtmp://1sd.sdo.sd.chinamobile.com/live/test");
    }

    @Test
    public void fileToRtmp() {
        toRtmp("/Users/hocgin/Downloads/test.mp4");
    }

    @SneakyThrows
    private void toRtmp(String frameSrc) {
        double frameRate = 25D;

        Loader.load(opencv_objdetect.class);

        // 本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
        FrameGrabber grabber = FrameGrabber.createDefault(0);

        // 开启抓取器
        grabber.start();

        // 转换器
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        // 抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
        IplImage grabbedImage = converter.convert(grabber.grab());
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        FrameRecorder recorder = FrameRecorder.createDefault(frameSrc, width, height);

        // avcodec.AV_CODEC_ID_H264，编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        // 封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFormat("flv");
        recorder.setFrameRate(frameRate);

        // 开启录制器
        recorder.start();
        long startTime = 0;
        long videoTS = 0;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        // 不知道为什么这里不做转换就不能推到rtmp
        Frame rotatedFrame = converter.convert(grabbedImage);
        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            rotatedFrame = converter.convert(grabbedImage);
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
        }
        frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
    }
}
