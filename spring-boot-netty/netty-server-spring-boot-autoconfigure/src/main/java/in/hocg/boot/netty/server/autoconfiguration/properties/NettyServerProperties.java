package in.hocg.boot.netty.server.autoconfiguration.properties;

import in.hocg.netty.core.constant.GlobalConstant;
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
@ConfigurationProperties(NettyServerProperties.PREFIX)
public class NettyServerProperties {
    public static final String PREFIX = "boot.netty.server";
    private Integer port = GlobalConstant.DEFAULT_PORT;

}
