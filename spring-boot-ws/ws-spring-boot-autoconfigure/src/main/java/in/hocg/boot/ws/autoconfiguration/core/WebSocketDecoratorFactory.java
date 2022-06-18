package in.hocg.boot.ws.autoconfiguration.core;

import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.ws.autoconfiguration.core.event.SocketClosedEvent;
import in.hocg.boot.ws.autoconfiguration.core.event.SocketConnectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String sessionId = session.getId();
                log.debug("建立连接: {}", sessionId);
                super.afterConnectionEstablished(session);
                SpringUtil.getApplicationContext().publishEvent(new SocketConnectedEvent(this, session));
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                String sessionId = session.getId();
                log.debug("连接: {}, 接收消息: {}", sessionId, message.getPayload());
                super.handleMessage(session, message);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                log.debug("接收到异常: ", exception);
                super.handleTransportError(session, exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                String sessionId = session.getId();
                log.debug("关闭连接: {}, 关闭原因: {}", sessionId, closeStatus);
                super.afterConnectionClosed(session, closeStatus);
                SpringUtil.getApplicationContext().publishEvent(new SocketClosedEvent(this, session, closeStatus));
            }
        };
    }

}
