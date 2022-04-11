package in.hocg.boot.javacv;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.javacv.autoconfiguration.support.FeatureHelper;
import in.hocg.boot.javacv.autoconfiguration.support.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class FeaturesTests {

    @Test
    public void test() throws IOException {
        BufferedImage filePair = ImageUtils.getBlackBufferedImage(300, 300);
    }

    @Test
    public void png2Video() throws IOException {
        URL dir = ResourceUtil.getResource("image2video");
        Path path = Files.createTempFile("test", ".mp4");
        FeatureHelper.pngToVideo(new File(dir.getPath()), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeVideo() throws IOException {
        URL dir = ResourceUtil.getResource("mergeVideo");
        Path path = Files.createTempFile("test", ".mp4");
        FeatureHelper.mergeVideo(new File(dir.getPath()), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeVideo2() throws IOException {
        URL dir = ResourceUtil.getResource("mergeVideo");
        Path path = Files.createTempFile("test", ".mp4");
        List<File> files = Lists.newArrayList(FileUtil.ls(dir.getPath()));

        FeatureHelper.mergeVideo(files, path.toFile(), 4 * 1000 * 1000, 4 * 1000 * 1000);
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeVideoStyle2() throws IOException {
        Path dir = Paths.get("/Users/hocgin/Downloads/test");
        Path path = new File(dir.toString(), "mergeVideoStyle2.test.mp4").toPath();

        List<File> files = Lists.newArrayList(FileUtil.ls(dir.toString())).stream()
            .filter(file -> file.getName().endsWith(".mp4"))
            .filter(file -> !file.getName().contains("test.mp4")).collect(Collectors.toList());

        FeatureHelper.mergeVideoStyle2(files, path.toFile(), 0, Convert.toLong(10 * 1000 * 1000));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void trimVideo() throws IOException {
        File file = new File("/Users/hocgin/Downloads/test/trim_7049587948383260000.mp4");
        FeatureHelper.trimVideo(new File("/Users/hocgin/Downloads/test/7049587948383260000.mp4"), 16.0 / 16, file);
        log.info("转换完成，路径：{}", file.toPath());
    }

    @Test
    public void fillVideo() throws IOException {
        File file = new File("/Users/hocgin/Downloads/test/trim_7049587948383260000.mp4");
        FeatureHelper.fillVideo(new File("/Users/hocgin/Downloads/test/7049587948383260000.mp4"), 1920, 1080, file);
        log.info("转换完成，路径：{}", file.toPath());
    }

    @Test
    public void mergeVideo4() throws IOException {
        Path dir = Paths.get("/Users/hocgin/Downloads/test");
        Path path = new File(dir.toString(), "mergeVideo4.test.mp4").toPath();

        List<File> files = Lists.newArrayList(FileUtil.ls(dir.toString())).stream()
            .filter(file -> file.getName().endsWith(".mp4"))
            .filter(file -> !file.getName().contains("test.mp4")).collect(Collectors.toList());

        FeatureHelper.mergeVideo(files, path.toFile(), 0, Convert.toLong(10 * 1000 * 1000));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeAudio() throws IOException {
        URL dir = ResourceUtil.getResource("mergeAudio");
        File firstFile = new File(dir.getPath());
        String suffix = "mp3";
        Path path = Files.createTempFile("test", "." + suffix);
        FeatureHelper.mergeAudio(firstFile, path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void addAudio() throws IOException {
        String dir = ResourceUtil.getResource("addAudio").getPath();
        Path path = Files.createTempFile("test", ".mp4");
        FeatureHelper.addAudio(new File(dir, "v0.mp4"), new File(dir, "b0.mp3"), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void videoToRtmp() {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        List<File> videoFiles = List.of(new File(dir, "v0.mp4"));
        FeatureHelper.videoToRtmp(videoFiles, "rtmp://160107.livepush.myqcloud.com/live/test?txSecret=8b02adfd4e78e5c588cdf5613f34fb6a&txTime=6243E65B");
    }

    @Test
    public void snapshot() throws IOException {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        File video = new File(dir, "v0.mp4");
        Path path = Files.createTempFile("snapshot", ".jpg");
        FeatureHelper.snapshot(video, 8 * (1000 * 1000), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void processing() throws IOException {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        File video = new File(dir, "v0.mp4");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String text = "HELLO WORLD";
        Path path = Files.createTempFile("erasure", ".mp4");

        // 处理每一帧
        FeatureHelper.processing(video, (bufferedImage, integer) -> {
            // 添加字幕时的时间
            Font font = new Font("微软雅黑", Font.BOLD, 32);
            AffineTransform affinetransform = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
            int textWidth = (int) (font.getStringBounds(text, frc).getWidth());
            int textHeight = (int) (font.getStringBounds(text, frc).getHeight());

            String timeContent = sdf.format(new Date());
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            //设置图片背景
            graphics.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);

            //设置左上方时间显示
            graphics.setColor(Color.orange);
            graphics.setFont(font);
            graphics.drawString(timeContent, 0, textHeight);

            // 计算文字长度，计算居中的x点坐标
            int widthX = (bufferedImage.getWidth() - textWidth) / 2;
            graphics.setColor(Color.red);
            graphics.setFont(font);
            graphics.drawString(text, widthX, bufferedImage.getHeight() - 100);
            graphics.dispose();
            return bufferedImage;
        }, path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void toAudio() throws IOException {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = Files.createTempFile("erasure", ".mp3");
        FeatureHelper.toAudio(new File(dir, "v0.mp4"), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void erasure() throws IOException {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = Files.createTempFile("erasure", ".mp4");
        FeatureHelper.erasure(new File(dir, "v0.mp4"), path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void subVideo() throws IOException {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = Files.createTempFile("subVideo", ".mp4");
        FeatureHelper.subVideo(new File(dir, "v0.mp4"), 2 * (1000 * 1000), 6 * (1000 * 1000),
            path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void toGif() throws IOException {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = Files.createTempFile("toGif", ".gif");
        FeatureHelper.toGif(new File(dir, "v0.mp4"), 2 * (1000 * 1000), 6 * (1000 * 1000),
            path.toFile());
        log.info("转换完成，路径：{}", path);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage bf = ImageUtils.getBlackBufferedImage(400, 400);
        Frame bgFrame = Java2DFrameUtils.toFrame(bf);
        BufferedImage bufferedImage = Java2DFrameUtils.toBufferedImage(bgFrame);
        ImageIO.write(bufferedImage, "png", new File("/Users/hocgin/Downloads/test.png"));
    }

}
