package in.hocg.sso.server.sample.config.security.user;

import in.hocg.sso.server.sample.config.security.user.authentication.token.TokenAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by hocgin on 2020/1/8.
 * email: hocgin@gmail.com
 * 配置
 *
 * @author hocgin
 */
@Configuration
public class AuthenticationConfigs {
    static final String LOGIN_SUCCESS_PAGE = "/index";
    static final String LOGIN_PAGE = "/login";

    public void configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        final AuthorizedSuccessHandle successHandler = new AuthorizedSuccessHandle(LOGIN_SUCCESS_PAGE);
        final AuthorizedFailureHandle failureHandle = new AuthorizedFailureHandle(LOGIN_PAGE);

        // ==== Form 表单 ====
        {
            http.formLogin()
                    .loginPage(LOGIN_PAGE)
                    .successHandler(successHandler)
                    .failureHandler(failureHandle)
                    .permitAll();
        }
        // ==== Token 登录方式 ====
        {
            http.addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
    }

    public void providers(AuthenticationManagerBuilder auth) {

    }

}
