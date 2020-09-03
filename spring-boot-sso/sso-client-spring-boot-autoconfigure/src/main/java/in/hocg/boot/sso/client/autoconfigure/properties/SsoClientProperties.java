package in.hocg.boot.sso.client.autoconfigure.properties;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@ToString
@ConfigurationProperties(prefix = SsoClientProperties.PREFIX)
public class SsoClientProperties {
    public static final String PREFIX = "boot.sso.client";
    private String loginProcessingUrl = "http://sso.hocgin.local:20000/login";
}
