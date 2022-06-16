package in.hocg.boot.notify.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(NotifyProperties.PREFIX)
public class NotifyProperties {
    public static final String PREFIX = "boot.notify";

}
