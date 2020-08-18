package in.hocg.sso.server.sample.config.security.user;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.web.result.ExceptionResult;
import in.hocg.boot.web.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2020/1/8.
 * email: hocgin@gmail.com
 * 登录验证失败
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuthorizedFailureHandle implements AuthenticationFailureHandler {
    private final String loginPage;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (IsAjaxRequestMatcher.THIS.matches(request)) {
            handleAjaxRequest(response);
        } else {
            response.sendRedirect(loginPage + "?error");
        }
    }

    private void handleAjaxRequest(HttpServletResponse response) throws IOException {
        log.warn("登录验证失败, 用户名或密码错误");
        ExceptionResult result = ExceptionResult.create(HttpServletResponse.SC_OK, ResultCode.PARAMS_ERROR.getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }
}
