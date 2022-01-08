package in.hocg.boot.ws.autoconfiguration.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class CommonHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        log.debug("握手前 Principal: {}", request.getPrincipal());
        // 用户鉴权
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
        log.debug("握手后");
    }
}
