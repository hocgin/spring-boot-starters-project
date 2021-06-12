package in.hocg.boot.vars.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(VarsProperties.PREFIX)
public class VarsProperties {
    public static final String PREFIX = "boot.vars";

}
