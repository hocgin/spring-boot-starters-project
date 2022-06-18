package in.hocg.boot.message.autoconfigure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(MessageProperties.PREFIX)
public class MessageProperties {
    public static final String PREFIX = "boot.message";

    /**
     * 消息类型
     */
    private MessageType type = MessageType.None;

    public enum MessageType {
        None,
        Rocket,
        Redis;
    }
}
