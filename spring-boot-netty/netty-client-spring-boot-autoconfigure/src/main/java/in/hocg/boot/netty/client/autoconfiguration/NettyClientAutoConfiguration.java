package in.hocg.boot.netty.client.autoconfiguration;

import in.hocg.boot.netty.client.autoconfiguration.properties.NettyClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = NettyClientProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyClientProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NettyClientAutoConfiguration implements ApplicationContextAware {
    private final NettyClientProperties properties;

    public static ConfigurableApplicationContext APPLICATION;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION = (ConfigurableApplicationContext) applicationContext;
    }

}
