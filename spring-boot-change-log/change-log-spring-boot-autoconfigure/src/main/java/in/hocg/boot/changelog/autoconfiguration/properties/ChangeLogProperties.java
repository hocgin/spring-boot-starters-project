package in.hocg.boot.changelog.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(ChangeLogProperties.PREFIX)
public class ChangeLogProperties {
    public static final String PREFIX = "boot.change-log";

}
