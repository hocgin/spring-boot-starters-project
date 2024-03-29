package in.hocg.sso2.server.sample.config.security.user;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.utils.struct.result.ExceptionResult;
import in.hocg.boot.utils.struct.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 * 匿名访问被拒绝
 *
 * @author hocgin
 */
@Slf4j
public class AjaxAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("匿名访问被拒绝", authException);
        ExceptionResult result = ExceptionResult.fail(HttpServletResponse.SC_UNAUTHORIZED, ResultCode.ACCESS_DENIED_ERROR.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }
}
