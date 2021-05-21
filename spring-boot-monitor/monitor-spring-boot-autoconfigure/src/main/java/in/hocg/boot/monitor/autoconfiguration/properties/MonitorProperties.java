package in.hocg.boot.monitor.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(MonitorProperties.PREFIX)
public class MonitorProperties {
    public static final String PREFIX = "boot.monitor";

}
