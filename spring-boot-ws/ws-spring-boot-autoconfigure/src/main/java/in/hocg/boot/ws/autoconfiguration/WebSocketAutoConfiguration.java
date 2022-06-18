package in.hocg.boot.ws.autoconfiguration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.ws.autoconfiguration.core.WebSocketDecoratorFactory;
import in.hocg.boot.ws.autoconfiguration.core.MessageExceptionAdvice;
import in.hocg.boot.ws.autoconfiguration.core.constant.StringConstants;
import in.hocg.boot.ws.autoconfiguration.core.handshake.AuthenticationHandshakeHandler;
import in.hocg.boot.ws.autoconfiguration.core.interceptor.CommonHandshakeInterceptor;
import in.hocg.boot.ws.autoconfiguration.core.service.SocketUserService;
import in.hocg.boot.ws.autoconfiguration.core.service.table.DefaultTableService;
import in.hocg.boot.ws.autoconfiguration.core.service.table.TableService;
import in.hocg.boot.ws.autoconfiguration.properties.WebSocketProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 * 在线测试:
 * http://www.easyswoole.com/wstool.html
 * 鉴权方案:
 * https://docs.spring.io/spring-security/site/docs/5.2.x/reference/html/integrations.html#websocket
 * http://www.moye.me/2017/02/10/websocket-authentication-and-authorization/
 * 开发方案:
 * https://spring.io/guides/gs/messaging-stomp-websocket/
 * https://www.cnblogs.com/dream-flying/articles/13019597.html
 * https://docs.spring.io/spring-framework/docs/4.3.x/spring-framework-reference/html/websocket.html
 * https://blog.csdn.net/weixin_33725270/article/details/88067111
 * https://blog.csdn.net/hry2015/article/details/79829616
 * <p>
 * 集群方案:
 * https://mp.weixin.qq.com/s/QeWb-9-j5EYeB7I37gZ50A
 *
 * @author hocgin
 */
@Configuration
@EnableWebSocketMessageBroker
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WebSocketProperties.class)
@Import(MessageExceptionAdvice.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {
    private final SocketUserService userService;
    private final WebSocketProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] endpoints = properties.getEndpoint().toArray(new String[]{});
        AuthenticationHandshakeHandler handshakeHandler = new AuthenticationHandshakeHandler(userService);
        CommonHandshakeInterceptor commonHandshakeInterceptor = new CommonHandshakeInterceptor(properties);
        String[] allowedOrigins = properties.getAllowedOrigins().toArray(new String[]{});

        if (properties.getWithSockJS()) {
            registry.addEndpoint(endpoints)
                .setHandshakeHandler(handshakeHandler)
                .addInterceptors(commonHandshakeInterceptor)
                .setAllowedOrigins(allowedOrigins)
                .withSockJS();
        }

        registry.addEndpoint(endpoints)
            .setHandshakeHandler(handshakeHandler)
            .addInterceptors(commonHandshakeInterceptor)
            .setAllowedOrigins(allowedOrigins);
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
        registry.setPathMatcher(new AntPathMatcher(StringConstants.PATH_SEPARATOR));

        // 配置消息代理，哪种路径的消息会进行代理处理
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new WebSocketDecoratorFactory())
            .setMessageSizeLimit(properties.getBufferSizeLimit())
            .setSendBufferSizeLimit(properties.getBufferSizeLimit())
            .setSendTimeLimit(10 * 10000);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        // ws 传输数据的时候，数据过大有时候会接收不到，所以在此处设置bufferSize
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(properties.getBufferSize());
        container.setMaxBinaryMessageBufferSize(properties.getBufferSize());
        container.setMaxSessionIdleTimeout(15 * 60000L);
        return container;
    }

    @Bean
    @ConditionalOnMissingBean
    public TableService tableService() {
        return new DefaultTableService();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }
}
