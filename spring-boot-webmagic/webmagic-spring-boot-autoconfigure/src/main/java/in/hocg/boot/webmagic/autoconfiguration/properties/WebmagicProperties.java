package in.hocg.boot.webmagic.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(WebmagicProperties.PREFIX)
public class WebmagicProperties {
    public static final String PREFIX = "boot.webmagic";

}
