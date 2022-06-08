package in.hocg.boot.ws.autoconfiguration.core.interceptor;

import in.hocg.boot.ws.autoconfiguration.properties.SocketProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class CommonHandshakeInterceptor implements HandshakeInterceptor {
    private final SocketProperties properties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        Principal principal = request.getPrincipal();
        log.debug("握手前 Principal: {}", principal);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
        log.debug("握手后");
    }
}
