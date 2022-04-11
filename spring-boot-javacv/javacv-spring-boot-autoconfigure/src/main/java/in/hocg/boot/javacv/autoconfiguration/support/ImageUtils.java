package in.hocg.boot.javacv.autoconfiguration.support;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by hocgin on 2022/4/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ImageUtils {

    @SneakyThrows
    public BufferedImage getBlackBufferedImage(int width, int height) {
        String text = "未完待续";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.BLACK);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

        Font font = new Font("Default", Font.BOLD, 35);
        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.drawRect(0, 0, width, height);

        int x = (width - metrics.stringWidth(text)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, x, y);
        graphics.dispose();
        return image;
    }

    @SneakyThrows
    public BufferedImage getBlackBufferedImage(BufferedImage image) {
        String text = "未完待续";
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.BLACK);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

        Font font = new Font("Default", Font.BOLD, 35);
        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.drawRect(0, 0, width, height);

        int x = (width - metrics.stringWidth(text)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, x, y);
        return image;
    }
}
