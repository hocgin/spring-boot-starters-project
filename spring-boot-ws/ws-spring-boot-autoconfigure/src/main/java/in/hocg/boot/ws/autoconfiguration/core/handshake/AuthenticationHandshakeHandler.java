package in.hocg.boot.ws.autoconfiguration.core.handshake;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.ws.autoconfiguration.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class AuthenticationHandshakeHandler extends DefaultHandshakeHandler {
    private final UserService userService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();

            // 登录逻辑
            String ticket = httpServletRequest.getParameter("ticket");
            if (StrUtil.isBlank(ticket)) {
                return userService.load(ticket);
            }
        }
        return null;
    }
}
