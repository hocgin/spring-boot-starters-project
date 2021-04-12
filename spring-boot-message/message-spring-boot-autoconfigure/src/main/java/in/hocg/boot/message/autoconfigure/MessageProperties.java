package in.hocg.boot.message.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(MessageProperties.PREFIX)
public class MessageProperties {
    public static final String PREFIX = "boot.message";

    /**
     * 消息类型
     */
    private MessageType type = MessageType.None;

    public enum MessageType {
        None,
        Rocket
    }
}
