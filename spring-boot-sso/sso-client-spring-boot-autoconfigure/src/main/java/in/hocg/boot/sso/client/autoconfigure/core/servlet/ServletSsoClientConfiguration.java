package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.sso.client.autoconfigure.core.AuthenticationResult;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import in.hocg.boot.utils.StringPoolUtils;
import in.hocg.boot.web.result.ExceptionResult;
import in.hocg.boot.web.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
        String[] denyUrls = properties.getDenyUrls().toArray(new String[]{});
        String[] authenticatedUrls = properties.getAuthenticatedUrls().toArray(new String[]{});
        Map<String, List<String>> hasAnyRole = properties.getHasAnyRole();
        Map<String, List<String>> hasAnyAuthority = properties.getHasAnyAuthority();
        Map<String, String> hasIpAddress = properties.getHasIpAddress();
        {
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
                http.authorizeRequests();

            // 如果配置需登陆
            if (authenticatedUrls.length > 0) {
                expressionInterceptUrlRegistry.antMatchers(authenticatedUrls).authenticated();
            }

            // 如果配置角色
            if (CollUtil.isNotEmpty(hasAnyRole)) {
                hasAnyRole.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && CollUtil.isNotEmpty(entry.getValue()))
                    .forEach(entry -> expressionInterceptUrlRegistry.antMatchers(entry.getKey()).hasAnyRole(ArrayUtil.toArray(entry.getValue(), String.class)));
            }

            // 如果配置权限
            if (CollUtil.isNotEmpty(hasAnyAuthority)) {
                hasAnyRole.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && CollUtil.isNotEmpty(entry.getValue()))
                    .forEach(entry -> expressionInterceptUrlRegistry.antMatchers(entry.getKey()).hasAnyAuthority(ArrayUtil.toArray(entry.getValue(), String.class)));
            }

            // 如果配置IP白名单
            if (CollUtil.isNotEmpty(hasIpAddress)) {
                hasIpAddress.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && StrUtil.isNotBlank(entry.getValue()))
                    .forEach(entry -> expressionInterceptUrlRegistry.antMatchers(entry.getKey()).hasIpAddress(entry.getValue()));
            }

            // 如果配置忽略
            if (ignoreUrls.length > 0) {
                expressionInterceptUrlRegistry.antMatchers(ignoreUrls).permitAll();
            }

            // 如果配置禁止访问
            if (denyUrls.length > 0) {
                expressionInterceptUrlRegistry.antMatchers(denyUrls).denyAll();
            }

            expressionInterceptUrlRegistry.anyRequest()
                .authenticated().and();
        }
        http.oauth2Login();
        http.exceptionHandling()
            .defaultAuthenticationEntryPointFor((request, response, authException) -> this.handleAuthentication4Servlet(request, response), ServletSsoClientConfiguration.IS_AJAX);

        http.csrf().disable();

        http.addFilterBefore(authenticationManager(getApplicationContext()), OAuth2AuthorizationRequestRedirectFilter.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletExpandAuthenticationManager authenticationManager(ApplicationContext applicationContext) {
        return new ServletExpandAuthenticationManager(applicationContext);
    }


    private final static RequestMatcher IS_AJAX = new RequestHeaderRequestMatcher(StringPoolUtils.HEADER_REQUESTED_WITH, StringPoolUtils.HEADER_VALUE_XMLHTTPREQUEST);

    private void handleAuthentication4Servlet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("匿名访问被拒绝");

        String redirectUrl = null;
        String xPageUrl = request.getHeader(StringPoolUtils.HEADER_PAGE_URL);
        if (StringUtils.isEmpty(xPageUrl)) {
            xPageUrl = request.getHeader(StringPoolUtils.HEADER_REFERER);
        }

        if (!StringUtils.isEmpty(xPageUrl)) {
            redirectUrl = xPageUrl;
        }

        AuthenticationResult result = AuthenticationResult.create(redirectUrl);

        this.setUtf8(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(result.toJSON());
        } catch (IOException e) {
            log.error("匿名访问被拒绝: ", e);
        }
    }

    private void handleAccessDenied4Servlet(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("登录后，访问被拒绝", accessDeniedException);
        ExceptionResult result = ExceptionResult.fail(HttpServletResponse.SC_UNAUTHORIZED, ResultCode.ACCESS_DENIED_ERROR.getMessage());
        this.setUtf8(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }

    /**
     * 指定输出 UTF-8
     *
     * @param response
     * @return
     */
    private HttpServletResponse setUtf8(ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return httpServletResponse;
    }
}
