package in.hocg.boot.sso2.client.autoconfigure;

import in.hocg.boot.sso2.client.autoconfigure.properties.SsoClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableOAuth2Sso
@EnableConfigurationProperties(SsoClientProperties.class)
@ConditionalOnProperty(prefix = SsoClientProperties.PREFIX, name = "enabled", matchIfMissing = true)
public class SsoClientAutoConfiguration {


//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/**")
//            .authorizeRequests()
//            .antMatchers("/ignore").permitAll()
//            .anyRequest()
//            .authenticated().and()
//        ;
//    }
//
//    @Bean
//    public OAuth2RestOperations restOperations(OAuth2ProtectedResourceDetails resource,
//                                               OAuth2ClientContext context) {
//        return new OAuth2RestTemplate(resource, context);
//    }
}
