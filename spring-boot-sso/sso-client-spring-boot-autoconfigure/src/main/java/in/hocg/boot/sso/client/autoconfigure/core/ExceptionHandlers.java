package in.hocg.boot.sso.client.autoconfigure.core;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.sso.client.autoconfigure.utils.ResponseUtils;
import in.hocg.boot.web.result.ExceptionResult;
import in.hocg.boot.web.result.ResultCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by hocgin on 2020/11/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class ExceptionHandlers {
    public final static RequestMatcher IS_AJAX = new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");

    public AuthenticationResult handleAuthentication4Servlet(HttpServletRequest request, HttpServletResponse response) {
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

    public void handleAccessDenied4Servlet(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("登录后，访问被拒绝", accessDeniedException);
        ExceptionResult result = ExceptionResult.fail(HttpServletResponse.SC_UNAUTHORIZED, ResultCode.ACCESS_DENIED_ERROR.getMessage());
        ResponseUtils.setUtf8(response);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }

    public Mono<Void> handleAuthentication4Webflux(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();

        String redirectUrl = null;
        String xPageUrl = headers.getFirst("X-Page-Url");
        if (StringUtils.isEmpty(xPageUrl)) {
            xPageUrl = headers.getFirst("Referer");
        }

        if (!StringUtils.isEmpty(xPageUrl)) {
            redirectUrl = xPageUrl;
        }

        AuthenticationResult result = AuthenticationResult.create(redirectUrl);
        DataBuffer buffer = response.bufferFactory()
            .wrap(result.toJSON().getBytes(StandardCharsets.UTF_8));
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(buffer));
    }
}
