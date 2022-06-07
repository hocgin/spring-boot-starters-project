package in.hocg.boot.ws.news.core;

import com.google.common.collect.Lists;
import in.hocg.boot.ws.news.handler.DebugStompSessionHandler;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2022/6/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class StompUtils {

    @SneakyThrows
    public StompSession getStompSession2(String url) {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        return stompClient.connect(url, new DebugStompSessionHandler()).get();
    }


    @SneakyThrows
    public StompSession getStompSession(String url) {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
            Lists.newArrayList(new WebSocketTransport(new StandardWebSocketClient()))));
        return stompClient.connect(url, new StompSessionHandlerAdapter(){}).get(10, TimeUnit.SECONDS);
    }

}
