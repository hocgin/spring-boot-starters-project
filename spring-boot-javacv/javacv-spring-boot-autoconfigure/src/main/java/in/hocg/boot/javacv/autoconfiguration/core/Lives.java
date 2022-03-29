package in.hocg.boot.javacv.autoconfiguration.core;

import cn.hutool.core.io.FileUtil;
import in.hocg.boot.javacv.autoconfiguration.utils.ImageUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by hocgin on 2022/1/9
 * email: hocgin@gmail.com
 * 直播
 *
 * @author hocgin
 */
@UtilityClass
public class Lives {

    @SneakyThrows
    public FFmpegFrameRecorder recorder(String rtmp, Boolean audioRecord) {
        int frameWidth = 480;
        int frameHeight = 270;

        FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault(rtmp, frameWidth, frameHeight);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        return recorder;
    }

    @SneakyThrows
    public static void main(String[] args) {
        File file = FileUtil.file("/Users/hocgin/Desktop/天气v1/PARTLY_CLOUDY_NIGHT.png");
        byte[] data = FileUtil.readBytes(file);

        FFmpegFrameRecorder recorder = recorder("rtmp://160107.livepush.myqcloud.com/live/sss?txSecret=c57c77436f7379d112680b6423a4e063&txTime=61DC304A", true);
        BufferedImage image = ImageUtils.bytesToBufferedImage(data);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        recorder.start();
        while (true) {
            recorder.record(converter.getFrame(image));
            Thread.sleep(500);
        }
//        recorder.stop();
    }
}
