package in.hocg.boot.netty.server.autoconfiguration;

import in.hocg.boot.netty.server.autoconfiguration.bean.CommandScanner;
import in.hocg.boot.netty.server.autoconfiguration.properties.NettyServerProperties;
import in.hocg.netty.server.netty.DefaultNettyServer;
import in.hocg.netty.server.netty.NettyServer;
import in.hocg.netty.server.netty.handler.AbsForwardHandler;
import in.hocg.netty.server.netty.handler.DefaultForwardHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = NettyServerProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyServerProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NettyServerAutoConfiguration implements InitializingBean {
    private final NettyServerProperties properties;

    @Bean
    @ConditionalOnMissingBean(NettyServer.class)
    public NettyServer nettyServer(AbsForwardHandler forwardHandler) {
        NettyServer server = new DefaultNettyServer(properties.getPort(), forwardHandler);
        server.start();
        return server;
    }

    @Bean
    @ConditionalOnMissingBean(CommandScanner.class)
    public CommandScanner commandScanner() {
        return new CommandScanner();
    }

    @Bean
    @ConditionalOnMissingBean(AbsForwardHandler.class)
    public AbsForwardHandler forwardHandler() {
        return new DefaultForwardHandler();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}.Port={}", this.getClass(), properties.getPort());
    }
}
