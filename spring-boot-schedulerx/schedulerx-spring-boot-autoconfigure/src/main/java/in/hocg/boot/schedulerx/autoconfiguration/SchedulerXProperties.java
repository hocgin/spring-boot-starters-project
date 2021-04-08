package in.hocg.boot.schedulerx.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(SchedulerXProperties.PREFIX)
public class SchedulerXProperties {
    public static final String PREFIX = "boot.scheduler-x";

}
