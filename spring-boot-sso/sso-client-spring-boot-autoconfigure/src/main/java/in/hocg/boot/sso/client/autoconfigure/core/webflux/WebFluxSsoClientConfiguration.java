package in.hocg.boot.sso.client.autoconfigure.core.webflux;

import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by hocgin on 2020/9/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@Import(SsoClientProperties.class)
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxSsoClientConfiguration {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2Client();
        http.oauth2Login();
        http.authorizeExchange().anyExchange().authenticated();
        return http.build();
    }
}
