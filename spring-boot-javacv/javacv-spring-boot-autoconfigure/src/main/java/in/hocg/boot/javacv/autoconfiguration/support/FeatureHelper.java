package in.hocg.boot.javacv.autoconfiguration.support;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

/**
 * Created by hocgin on 2022/3/29
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class FeatureHelper {

    public Path png2Video(File dir) {
        File[] files = FileUtil.ls(dir.getAbsolutePath());
        return png2Video(List.of(files));
    }

    @SneakyThrows
    public Path png2Video(List<File> files) {
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
}
