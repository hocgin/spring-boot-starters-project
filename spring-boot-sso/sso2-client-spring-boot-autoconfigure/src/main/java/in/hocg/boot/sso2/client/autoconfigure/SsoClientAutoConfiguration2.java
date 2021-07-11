//package in.hocg.boot.sso.client2.autoconfigure;
//
//import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
//import in.hocg.boot.sso.client2.autoconfigure.core.ClientResourceServerConfigurer;
//import in.hocg.boot.sso.client2.autoconfigure.core.authentication.RedirectUrlAuthenticationEntryPoint;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.web.client.DefaultResponseErrorHandler;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.LinkedHashMap;
//
///**
// * Created by hocgin on 2019/6/12.
// * email: hocgin@gmail.com
// *
// * @author hocgin
// */
//@Slf4j
//@Configuration
//@EnableOAuth2Sso
//@EnableOAuth2Client
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableConfigurationProperties(SsoClientProperties.class)
//@RequiredArgsConstructor(onConstructor = @__(@Lazy))
//public class SsoClientAutoConfiguration2 {
//    private final SsoClientProperties properties;
//
//    @Bean
//    @Primary
//    @LoadBalanced
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
//
//    @Bean
//    @ConditionalOnMissingBean
//    public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
//        return new AccessTokenContextRelay(context);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public ResourceServerTokenServices tokenServices(ResourceServerProperties properties) {
//        RemoteTokenServices remoteService = new RemoteTokenServices();
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
//        remoteService.setAccessTokenConverter(accessTokenConverter);
//        remoteService.setClientId(properties.getClientId());
//        remoteService.setClientSecret(properties.getClientSecret());
//        remoteService.setCheckTokenEndpointUrl(properties.getTokenInfoUri());
//        return remoteService;
//    }
//
//    @Bean
//    public ResourceServerConfigurerAdapter resourceServerConfigurer(RemoteTokenServices remoteTokenServices,
//                                                                    AuthenticationEntryPoint authenticationEntryPoint,
//                                                                    RestTemplate restTemplate) {
//        return new ClientResourceServerConfigurer(remoteTokenServices, restTemplate, authenticationEntryPoint);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        RedirectUrlAuthenticationEntryPoint authenticationEntryPoint = new RedirectUrlAuthenticationEntryPoint();
//
//        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<RequestMatcher, AuthenticationEntryPoint>() {{
//            put(request -> true, authenticationEntryPoint);
//        }};
//        DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
//        delegatingAuthenticationEntryPoint.setDefaultEntryPoint(authenticationEntryPoint);
//        return delegatingAuthenticationEntryPoint;
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public OAuth2RestTemplate oAuth2RestTemplate() {
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
//    }
//
//}
