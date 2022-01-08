package in.hocg.boot.ws.autoconfiguration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.ws.autoconfiguration.core.handshake.AuthenticationHandshakeHandler;
import in.hocg.boot.ws.autoconfiguration.core.interceptor.CommonHandshakeInterceptor;
import in.hocg.boot.ws.autoconfiguration.core.service.WebSocketUserService;
import in.hocg.boot.ws.autoconfiguration.properties.WebSocketProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WebSocketProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {
    private final WebSocketProperties properties;
    private final WebSocketUserService userService;

    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(properties.getEndpoint().toArray(new String[]{}))
            .setHandshakeHandler(new AuthenticationHandshakeHandler(userService))
            .addInterceptors(new CommonHandshakeInterceptor(properties))
            .setAllowedOrigins(properties.getAllowedOrigins().toArray(new String[]{}))
//            .withSockJS()
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        List<String> destinationPrefix = properties.getDestinationPrefix();
        if (CollUtil.isNotEmpty(destinationPrefix)) {
            registry.enableSimpleBroker(destinationPrefix.toArray(new String[]{}));
        }
        String userDestinationPrefix = properties.getUserDestinationPrefix();
        if (StrUtil.isNotBlank(userDestinationPrefix)) {
            registry.setUserDestinationPrefix(userDestinationPrefix);
        }
        String appDestinationPrefix = properties.getAppDestinationPrefix();
        if (StrUtil.isNotBlank(appDestinationPrefix)) {
            registry.setApplicationDestinationPrefixes(appDestinationPrefix);
        }

        // 配置消息代理，哪种路径的消息会进行代理处理
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
//        registry.addDecoratorFactory(new WebSocketDecoratorFactory(tableService));
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }
}
