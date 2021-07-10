package in.hocg.sso.server2.sample.config.security.config;

import in.hocg.sso.server2.sample.config.security.user.AjaxAccessDeniedHandler;
import in.hocg.sso.server2.sample.config.security.user.AjaxAuthenticationEntryPoint;
import in.hocg.sso.server2.sample.config.security.user.AuthenticationConfigs;
import in.hocg.sso.server2.sample.config.security.user.IsAjaxRequestMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by hocgin on 2020/1/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final AuthenticationConfigs authenticationConfigs;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 基础信息配置
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated().and()
            .csrf().disable()
            .cors().disable()
        ;
        http.formLogin().permitAll();

        // 异常处理配置(这边针对AJAX进行不同处理，如果不需要可以忽略)
        http.exceptionHandling()
            .defaultAuthenticationEntryPointFor(new AjaxAuthenticationEntryPoint(), new IsAjaxRequestMatcher())
            .defaultAccessDeniedHandlerFor(new AjaxAccessDeniedHandler(), new IsAjaxRequestMatcher());

        // 登陆相关配置
        authenticationConfigs.configure(http, authenticationManager());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定处理如何处理用户登陆请求
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/check_token");
        web.debug(true);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
