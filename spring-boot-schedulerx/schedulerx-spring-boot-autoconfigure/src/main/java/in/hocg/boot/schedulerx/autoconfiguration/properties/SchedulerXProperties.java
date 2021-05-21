package in.hocg.boot.schedulerx.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(SchedulerXProperties.PREFIX)
public class SchedulerXProperties {
    public static final String PREFIX = "boot.schedulerx2";

}
