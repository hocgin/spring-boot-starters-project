package in.hocg.boot.javacv;

import cn.hutool.core.io.resource.ResourceUtil;
import in.hocg.boot.javacv.autoconfiguration.support.FeatureHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class FeaturesTests {

    @Test
    public void png2Video() {
        URL dir = ResourceUtil.getResource("image2video");
        Path path = FeatureHelper.pngToVideo(new File(dir.getPath()));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeVideo() {
        URL dir = ResourceUtil.getResource("mergeVideo");
        Path path = FeatureHelper.mergeVideo(new File(dir.getPath()));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void mergeAudio() {
        URL dir = ResourceUtil.getResource("mergeAudio");
        Path path = FeatureHelper.mergeAudio(new File(dir.getPath()));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void addAudio() {
        String dir = ResourceUtil.getResource("addAudio").getPath();
        Path path = FeatureHelper.addAudio(new File(dir, "v0.mp4"), new File(dir, "b0.mp3"));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void videoToRtmp() {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        List<File> videoFiles = List.of(new File(dir, "v0.mp4"));
        FeatureHelper.videoToRtmp(videoFiles, "rtmp://160107.livepush.myqcloud.com/live/test?txSecret=8b02adfd4e78e5c588cdf5613f34fb6a&txTime=6243E65B");
    }

    @Test
    public void snapshot() {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        File video = new File(dir, "v0.mp4");
        Path path = FeatureHelper.snapshot(video, 8 * (1000 * 1000));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void processing() {
        String dir = ResourceUtil.getResource("videoToRtmp").getPath();
        File video = new File(dir, "v0.mp4");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String text = "HELLO WORLD";

        // 处理每一帧
        Path path = FeatureHelper.processing(video, (bufferedImage, integer) -> {
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
        });
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void toAudio() {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = FeatureHelper.toAudio(new File(dir, "v0.mp4"));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void erasure() {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = FeatureHelper.erasure(new File(dir, "v0.mp4"));
        log.info("转换完成，路径：{}", path);
    }

    @Test
    public void toGif() {
        String dir = ResourceUtil.getResource("erasure").getPath();
        Path path = FeatureHelper.toGif(new File(dir, "v0.mp4"), 2 * (1000 * 1000), 6 * (1000 * 1000));
        log.info("转换完成，路径：{}", path);
    }
}
