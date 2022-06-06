package in.hocg.boot.ws.news;

import in.hocg.boot.ws.news.core.StompUtils;
import in.hocg.boot.ws.news.handler.DebugStompSessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.MimeTypeUtils;

/**
 * Created by hocgin on 2022/6/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class SenderTests {
    static StompSession session;

    @BeforeAll
    public static void before() {
        session = StompUtils.getStompSession("ws://127.0.0.1:8080/ws");
    }

    @Test
    public void sendTextMessage() {
        session.send("/all", "hello world".getBytes());
    }

    @Test
    public void sendGet() {
        String destination = "/index";
        session.subscribe(destination, new DebugStompSessionHandler());


        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.setContentType(MimeTypeUtils.APPLICATION_JSON);
        session.send(headers, "{}".getBytes());
    }
}
