package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.sso.client.autoconfigure.core.AuthenticationResult;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import in.hocg.boot.web.result.ExceptionResult;
import in.hocg.boot.web.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
            .defaultAuthenticationEntryPointFor((request, response, authException) -> this.handleAuthentication4Servlet(request, response), ServletSsoClientConfiguration.IS_AJAX);

        http.csrf().disable();
    }

    private final static RequestMatcher IS_AJAX = new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");

    private AuthenticationResult handleAuthentication4Servlet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("匿名访问被拒绝");

        String redirectUrl = null;
        String xPageUrl = request.getHeader("X-Page-Url");
        if (StringUtils.isEmpty(xPageUrl)) {
            xPageUrl = request.getHeader("Referer");
        }

        if (!StringUtils.isEmpty(xPageUrl)) {
            redirectUrl = xPageUrl;
        }

        AuthenticationResult result = AuthenticationResult.create(redirectUrl);

        ResponseUtils.setUtf8(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(result.toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void handleAccessDenied4Servlet(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("登录后，访问被拒绝", accessDeniedException);
        ExceptionResult result = ExceptionResult.fail(HttpServletResponse.SC_UNAUTHORIZED, ResultCode.ACCESS_DENIED_ERROR.getMessage());
        ResponseUtils.setUtf8(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }

}
