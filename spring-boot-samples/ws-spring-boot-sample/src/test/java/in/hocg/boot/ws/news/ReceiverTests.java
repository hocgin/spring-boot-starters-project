package in.hocg.boot.ws.news;

import in.hocg.boot.ws.news.core.StompUtils;
import in.hocg.boot.ws.news.handler.DebugStompSessionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Created by hocgin on 2022/6/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class ReceiverTests {
    static StompSession session;

    @BeforeAll
    @SneakyThrows
    public static void before() {
        session = StompUtils.getStompSession("ws://127.0.0.1:8080/ws");
    }

    @Test
    @SneakyThrows
    public void subscribe() {
        session.subscribe("/queue/all", new DebugStompSessionHandler());

        Thread.sleep(100L * 1000);
    }


}
