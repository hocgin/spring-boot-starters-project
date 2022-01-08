package in.hocg.boot.ws;


import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import java.net.URI;

/**
 * Created by hocgin on 2022/1/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class WebSocketClientImpl implements WebSocketClient {
    @Override
    public ListenableFuture<WebSocketSession> doHandshake(WebSocketHandler webSocketHandler, String uriTemplate, Object... uriVariables) {
        return null;
    }

    @Override
    public ListenableFuture<WebSocketSession> doHandshake(WebSocketHandler webSocketHandler, WebSocketHttpHeaders headers, URI uri) {
        return null;
    }
}
