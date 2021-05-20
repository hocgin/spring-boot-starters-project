package in.hocg.boot.webmagic.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(WebMagicProperties.PREFIX)
public class WebMagicProperties {
    public static final String PREFIX = "boot.web-magic";

}
