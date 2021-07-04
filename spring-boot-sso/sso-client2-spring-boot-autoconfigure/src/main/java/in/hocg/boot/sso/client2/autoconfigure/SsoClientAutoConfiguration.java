package in.hocg.boot.sso.client.autoconfigure;

import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import in.hocg.boot.sso.client2.autoconfigure.core.ClientResourceServerConfigurer;
import in.hocg.boot.sso.client2.autoconfigure.core.ResourceAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableResourceServer
@AutoConfigureAfter(OAuth2AutoConfiguration.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SsoClientProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SsoClientAutoConfiguration {
    private final SsoClientProperties properties;

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate lbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
        return new AccessTokenContextRelay(context);
    }

    @Bean
    public ResourceServerConfigurerAdapter resourceServerConfigurer(RemoteTokenServices remoteTokenServices,
                                                                    AuthenticationEntryPoint authenticationEntryPoint,
                                                                    RestTemplate restTemplate) {
        return new ClientResourceServerConfigurer(remoteTokenServices, restTemplate, authenticationEntryPoint);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteTokenServices remoteTokenServices(ResourceServerProperties properties) {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
        remoteTokenServices.setClientId(properties.getClientId());
        remoteTokenServices.setClientSecret(properties.getClientSecret());
        remoteTokenServices.setCheckTokenEndpointUrl(properties.getTokenInfoUri());
        return remoteTokenServices;
    }


    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        ResourceAuthenticationEntryPoint authenticationEntryPoint = new ResourceAuthenticationEntryPoint();

        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<RequestMatcher, AuthenticationEntryPoint>() {{
            put(request -> true, authenticationEntryPoint);
        }};
        DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        delegatingAuthenticationEntryPoint.setDefaultEntryPoint(authenticationEntryPoint);
        return delegatingAuthenticationEntryPoint;
    }

}
