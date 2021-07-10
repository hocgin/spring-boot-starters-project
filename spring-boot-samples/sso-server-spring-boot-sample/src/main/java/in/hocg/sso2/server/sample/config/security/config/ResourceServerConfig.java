package in.hocg.sso2.server.sample.config.security.config;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.StringPoolUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
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
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(this.isAuthorizationRequestMatcher())
            .authorizeRequests().anyRequest().authenticated();
    }

    private RequestMatcher isAuthorizationRequestMatcher() {
        return request -> {
            String authorization = request.getHeader(StringPoolUtils.HEADER_AUTHORIZATION);
            return Objects.nonNull(authorization)
                && (StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BEARER)
                || StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BASIC));
        };
    }
}
