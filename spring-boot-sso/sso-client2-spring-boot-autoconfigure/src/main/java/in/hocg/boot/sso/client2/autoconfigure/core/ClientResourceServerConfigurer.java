package in.hocg.boot.sso.client2.autoconfigure.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hocgin on 2021/7/3
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ClientResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    private final RemoteTokenServices remoteTokenServices;
    private final RestTemplate restTemplate;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        remoteTokenServices.setRestTemplate(restTemplate);
        resources.tokenServices(remoteTokenServices)
            .authenticationEntryPoint(authenticationEntryPoint);
    }

}
