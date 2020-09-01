package in.hocg.sso2.server.sample.config.security.user;

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
        http.csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .anyRequest().authenticated().and()
        ;
//        http.logout().permitAll();

        http.formLogin().permitAll();

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.exceptionHandling()
//            .defaultAuthenticationEntryPointFor(new AjaxAuthenticationEntryPoint(), new IsAjaxRequestMatcher())
//            .defaultAccessDeniedHandlerFor(new AjaxAccessDeniedHandler(), new IsAjaxRequestMatcher());

//        authenticationConfigs.configure(http, authenticationManagerBean());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        authenticationConfigs.providers(auth);
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
