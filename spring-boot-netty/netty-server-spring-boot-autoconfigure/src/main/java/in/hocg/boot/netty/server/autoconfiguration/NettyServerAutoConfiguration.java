package in.hocg.boot.netty.server.autoconfiguration;

import in.hocg.boot.netty.server.autoconfiguration.properties.NettyServerProperties;
import in.hocg.netty.server.netty.DefaultNettyServer;
import in.hocg.netty.server.netty.NettyServer;
import in.hocg.netty.server.netty.handler.AbsForwardHandler;
import in.hocg.netty.server.netty.handler.DefaultForwardHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = NettyServerProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyServerProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NettyServerAutoConfiguration implements ApplicationContextAware {
    private final NettyServerProperties properties;

    public static ConfigurableApplicationContext APPLICATION;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION = (ConfigurableApplicationContext) applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(NettyServer.class)
    public NettyServer nettyServer(AbsForwardHandler forwardHandler) {
        NettyServer server = new DefaultNettyServer(properties.getPort(), forwardHandler);
        server.start();
        return server;
    }

    @Bean
    @ConditionalOnMissingBean(AbsForwardHandler.class)
    public AbsForwardHandler forwardHandler() {
        return new DefaultForwardHandler();
    }
}
