package in.hocg.boot.tpl.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(TplProperties.PREFIX)
public class TplProperties {
    public static final String PREFIX = "boot.tpl";

}
