package in.hocg.boot.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class WebSocketTests {

    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        StompSession session = stompClient.connect("ws://127.0.0.1:21000/ws", new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {

            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

            }
        }).get();

        session.subscribe("/user/queue/errors", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.debug("{}", headers);
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.debug("{}", payload);
            }
        });

        session.subscribe("/queue/errors", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.debug("{}", headers);
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.debug("{}", payload);
            }
        });

        session.send("/app/index", "new Object()".getBytes(StandardCharsets.UTF_8));

        System.out.println("client.run()");
        Thread.sleep(100 * 1000);
    }
}
