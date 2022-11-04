package in.hocg.boot.cps.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(CpsProperties.PREFIX)
public class CpsProperties {
    public static final String PREFIX = "boot.cps";

}
