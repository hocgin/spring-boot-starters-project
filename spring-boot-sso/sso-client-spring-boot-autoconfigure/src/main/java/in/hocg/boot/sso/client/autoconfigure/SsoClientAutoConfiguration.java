package in.hocg.boot.sso.client.autoconfigure;

import in.hocg.boot.sso.client.autoconfigure.core.servlet.ServletSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.WebFluxSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import in.hocg.boot.sso.client.autoconfigure.utils.AuthoritiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SsoClientProperties.class)
@Import({ServletSsoClientConfiguration.class, WebFluxSsoClientConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SsoClientAutoConfiguration {
    private final SsoClientProperties properties;

//    @Bean
//    @Primary
//    public RestTemplate lbRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//            @Override
//            public void handleError(ClientHttpResponse response) throws IOException {
//                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
//                    super.handleError(response);
//                }
//            }
//        });
//        return restTemplate;
//    }
}
