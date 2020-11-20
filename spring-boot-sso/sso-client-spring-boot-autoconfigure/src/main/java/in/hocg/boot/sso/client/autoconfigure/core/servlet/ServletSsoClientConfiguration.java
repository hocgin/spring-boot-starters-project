package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import in.hocg.boot.sso.client.autoconfigure.core.ExceptionHandlers;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * Created by hocgin on 2020/9/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletSsoClientConfiguration extends WebSecurityConfigurerAdapter {
    private final SsoClientProperties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] ignoreUrls = properties.getIgnoreUrls().toArray(new String[]{});
        {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
                http.authorizeRequests();
            if (ignoreUrls.length > 0) {
                expressionInterceptUrlRegistry.antMatchers(ignoreUrls).permitAll();
            }
            expressionInterceptUrlRegistry
                .anyRequest()
                .authenticated().and();
        }
        http.oauth2Login();
        http.exceptionHandling()
            .defaultAuthenticationEntryPointFor((request, response, authException) -> ExceptionHandlers.handleAuthentication4Servlet(request, response), ExceptionHandlers.IS_AJAX);

        http.csrf().disable();
    }

}
