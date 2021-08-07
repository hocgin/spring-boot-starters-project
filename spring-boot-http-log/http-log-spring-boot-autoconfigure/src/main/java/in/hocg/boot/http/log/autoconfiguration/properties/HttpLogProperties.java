package in.hocg.boot.http.log.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(HttpLogProperties.PREFIX)
public class HttpLogProperties {
    public static final String PREFIX = "boot.http.log";

}
