package in.hocg.boot.ws.news.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

/**
 * Created by hocgin on 2022/6/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class DebugStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        log.debug("afterConnected");
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        log.debug("handleException");
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        log.debug("handleTransportError");
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        log.debug("getPayloadType");
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        log.debug("handleFrame");
    }
}
