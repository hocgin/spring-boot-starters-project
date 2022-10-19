package in.hocg.boot.netty.client.autoconfiguration;

import in.hocg.boot.netty.client.autoconfiguration.properties.NettyClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@ConditionalOnProperty(prefix = NettyClientProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyClientProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NettyClientAutoConfiguration implements InitializingBean {
    private final NettyClientProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}.Port={}", this.getClass(), properties.getPort());
    }
}
