package in.hocg.sso.server2.sample.config.security.config;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.StringPoolUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Objects;

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
        http.requestMatcher(this.isAuthorizationRequestMatcher())
            .authorizeRequests()
            .anyRequest().permitAll();
    }

    private RequestMatcher isAuthorizationRequestMatcher() {
        return request -> {
            String authorization = request.getHeader(StringPoolUtils.HEADER_AUTHORIZATION);
            return new AntPathRequestMatcher("/oauth/**").matches(request) ||
                (Objects.nonNull(authorization) && (StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BEARER)
                    || StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BASIC)));
        };
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.stateless(true);
    }
}
