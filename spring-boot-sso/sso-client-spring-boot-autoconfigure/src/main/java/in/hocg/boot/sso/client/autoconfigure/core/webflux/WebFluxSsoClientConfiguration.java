package in.hocg.boot.sso.client.autoconfigure.core.webflux;

import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxSsoClientConfiguration {
    private final SsoClientProperties properties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        String[] ignoreUrls = properties.getIgnoreUrls().toArray(new String[]{});
        {
            ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec =
                http.authorizeExchange();
            if (ignoreUrls.length > 0) {
                authorizeExchangeSpec.pathMatchers(ignoreUrls).permitAll();
            }
            authorizeExchangeSpec
                .anyExchange()
                .authenticated().and();
        }
        http.oauth2Login();

        return http.build();
    }
}
