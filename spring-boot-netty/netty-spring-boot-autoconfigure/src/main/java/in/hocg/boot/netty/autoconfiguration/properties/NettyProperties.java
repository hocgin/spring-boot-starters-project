package in.hocg.boot.netty.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(NettyProperties.PREFIX)
public class NettyProperties {
    public static final String PREFIX = "boot.netty";

}
