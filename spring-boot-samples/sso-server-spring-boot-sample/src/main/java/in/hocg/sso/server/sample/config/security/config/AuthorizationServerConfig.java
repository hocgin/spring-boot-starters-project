package in.hocg.sso.server.sample.config.security.config;


import in.hocg.sso.server.sample.config.security.autoconfiguration.SsoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * Created by hocgin on 2020/1/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final SsoProperties properties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // 这边使用内存的方式来存储，单点客户端信息
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        for (SsoProperties.Client client : properties.getClients()) {
            builder.withClient(client.getClientId())
                .secret(passwordEncoder.encode(client.getClientSecret()))
                .authorizedGrantTypes("client_credentials", "authorization_code", "refresh_token", "password")
                .scopes("user_info")
                .redirectUris(client.getRedirectUris())
                .autoApprove(true);
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService);
    }



}
