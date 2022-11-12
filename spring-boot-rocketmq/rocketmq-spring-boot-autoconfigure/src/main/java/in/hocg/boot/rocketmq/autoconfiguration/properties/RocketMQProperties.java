package in.hocg.boot.rocketmq.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(RocketMQProperties.PREFIX)
public class RocketMQProperties {
    public static final String PREFIX = "boot.rocketmq";

}
