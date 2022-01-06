package in.hocg.boot.ws.autoconfiguration;

import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.ws.autoconfiguration.core.WebSocketDecoratorFactory;
import in.hocg.boot.ws.autoconfiguration.core.handshake.AuthenticationHandshakeHandler;
import in.hocg.boot.ws.autoconfiguration.core.interceptor.WsHandshakeInterceptor;
import in.hocg.boot.ws.autoconfiguration.core.service.UserService;
import in.hocg.boot.ws.autoconfiguration.core.service.table.DefaultTableService;
import in.hocg.boot.ws.autoconfiguration.core.service.table.TableService;
import in.hocg.boot.ws.autoconfiguration.properties.WsProperties;
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
@ConditionalOnProperty(prefix = WsProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WsProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WsAutoConfiguration implements WebSocketMessageBrokerConfigurer {
    private final TableService tableService;
    private final UserService userService;

    @Bean
    @ConditionalOnMissingBean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setHandshakeHandler(new AuthenticationHandshakeHandler(userService))
            .addInterceptors(new WsHandshakeInterceptor())
            .setAllowedOrigins("*")
//            .withSockJS()
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 广播通道
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/app");

//        registry.enableStompBrokerRelay("/queue", "/topic");

        // 配置消息代理，哪种路径的消息会进行代理处理
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new WebSocketDecoratorFactory(tableService));
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
