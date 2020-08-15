package in.hocg.boot.knife.gateway.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = KnifeGatewayProperties.PREFIX)
public class KnifeGatewayProperties {
    public static final String PREFIX = "boot.knife.gateway";
}
