package in.hocg.boot.sso.client2.autoconfigure.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hocgin on 2021/7/3
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class ResourceAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("触发授权异常: ", authException);
        // 如果是ajax

        // 如果不是ajax


    }
}
