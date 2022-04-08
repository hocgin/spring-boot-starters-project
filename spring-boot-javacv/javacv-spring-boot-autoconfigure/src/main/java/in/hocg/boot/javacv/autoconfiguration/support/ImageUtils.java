package in.hocg.boot.javacv.autoconfiguration.support;

import cn.hutool.core.lang.Pair;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by hocgin on 2022/4/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ImageUtils {

    @SneakyThrows
    public Pair<BufferedImage, File> getBlackBufferedImage(int width, int height) {
        String text = "未完待续";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);

        Font font = new Font("微软雅黑", Font.BOLD, 35);
        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.drawRect(0, 0, width, height);

        int x = (width - metrics.stringWidth(text)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, x, y);
        File file = File.createTempFile("test", ".png");
        ImageIO.write(image, "png", file);
        return new Pair(image, file);
    }
}
