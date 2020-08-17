package in.hocg.sso.client.sample.config.security;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.web.result.ExceptionResult;
import in.hocg.boot.web.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2020/8/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.warn("登录后，访问被拒绝", e);
        ExceptionResult result = ExceptionResult.create(HttpServletResponse.SC_UNAUTHORIZED, ResultCode.ACCESS_DENIED_ERROR.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(JSONUtil.toJsonStr(result));
        }
    }
}
