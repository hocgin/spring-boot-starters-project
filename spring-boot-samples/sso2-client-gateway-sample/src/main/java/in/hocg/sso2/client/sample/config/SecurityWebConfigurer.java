package in.hocg.sso2.client.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by hocgin on 2020/8/27
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
//@Configuration
//@EnableWebFluxSecurity
public class SecurityWebConfigurer {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2Client();
        http.oauth2Login();
        http.authorizeExchange().anyExchange().authenticated();
        return http.build();
    }

}
