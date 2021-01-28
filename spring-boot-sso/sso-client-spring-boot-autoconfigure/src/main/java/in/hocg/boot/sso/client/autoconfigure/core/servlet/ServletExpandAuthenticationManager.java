package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.sso.client.autoconfigure.core.BearerTokenAuthentication;
import in.hocg.boot.sso.client.autoconfigure.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class ServletExpandAuthenticationManager extends OncePerRequestFilter {
    private final ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = TokenUtils.resolveFromAuthorizationHeader(authorization);
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        BearerTokenAuthentication tokenAuthentication = context.getBean(BearerTokenAuthentication.class);
        Assert.notNull(tokenAuthentication);
        Authentication authentication = tokenAuthentication.authentication(token);
        if (Objects.isNull(authentication)) {
            authentication = TokenUtils.ANONYMOUS_AUTHENTICATION_TOKEN;
        }
        final SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
