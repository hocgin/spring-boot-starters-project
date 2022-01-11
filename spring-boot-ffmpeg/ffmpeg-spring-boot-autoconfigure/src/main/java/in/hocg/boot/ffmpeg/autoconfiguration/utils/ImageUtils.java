package in.hocg.boot.ffmpeg.autoconfiguration.utils;

import lombok.experimental.UtilityClass;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by hocgin on 2022/1/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ImageUtils {

    /**
     * 图片转数组
     *
     * @param image
     * @param format
     * @return
     */
    public byte[] bufferedImageToBytes(BufferedImage image, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    /**
     * 数组转图片
     *
     * @param bytes
     * @return
     */
    public BufferedImage bytesToBufferedImage(byte[] bytes) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            return null;
        }
    }

    public static BufferedImage iplImageToBufferedImage(IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return converter.getBufferedImage(frame, 1);
    }

    public static IplImage bufferedImageToIplImage(BufferedImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter jconverter = new Java2DFrameConverter();
        return grabberConverter.convert(jconverter.convert(src));
    }

    public static Frame bufferedImageToFrame(BufferedImage src) {
        return new Java2DFrameConverter().convert(src);
    }

}
