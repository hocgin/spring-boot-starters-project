package in.hocg.sso.server2.sample.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hocgin on 2020/1/7.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(this.withUseOAuth()).authorizeRequests()
            .anyRequest().permitAll();
    }

    private RequestMatcher withUseOAuth() {
        return new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return request.getHeader("Authorization") != null;
            }
        };
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.stateless(true);
    }
}
