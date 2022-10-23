package in.hocg.boot.kafka.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(KafkaProperties.PREFIX)
public class KafkaProperties {
    public static final String PREFIX = "boot.kafka";

}
