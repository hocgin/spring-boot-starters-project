package in.hocg.boot.task.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(TaskProperties.PREFIX)
public class TaskProperties {
    public static final String PREFIX = "boot.task";

}
