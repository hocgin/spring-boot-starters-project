package in.hocg.boot.ws.autoconfiguration.core;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.ws.autoconfiguration.core.service.table.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import javax.el.MethodNotFoundException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {
    private final TableService tableService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String sessionId = session.getId();
                log.debug("建立连接: {}", sessionId);
                tableService.create(sessionId);
                super.afterConnectionEstablished(session);
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                String sessionId = session.getId();
                log.debug("连接: {}, 接收消息: {}", sessionId, message);
                tableService.heartbeat(sessionId);
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
                log.debug("关闭连接: {}", sessionId);
                tableService.remove(sessionId);
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }


    private Object callMatchMethod(String url, String body) {
        Map<String, Object> controller = SpringUtil.getApplicationContext().getBeansWithAnnotation(Controller.class);
        for (Map.Entry<String, Object> entry : controller.entrySet()) {
            Object value = entry.getValue();
            Optional<Method> methodOpt = getMethod(value.getClass(), url);
            if (methodOpt.isPresent()) {
                return ReflectUtil.invoke(value, methodOpt.get(), body);
            }
        }
        throw new MethodNotFoundException("没有找到匹配的方法");
    }

    public Optional<Method> getMethod(Class<?> clazz, String url) {
        return Arrays.stream(ReflectUtil.getPublicMethods(clazz))
            .filter(method -> {
                boolean hasAnnotation = method.isAnnotationPresent(RequestMapping.class);
                if (!hasAnnotation) {
                    return false;
                }
                boolean hasOkArgs = method.getParameterCount() <= 1;
                if (!hasOkArgs) {
                    return false;
                }

                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String[] values = annotation.value();
                for (String value1 : values) {
                    if (new AntPathMatcher().match(value1, url)) {
                        return true;
                    }
                }
                return false;
            }).findFirst();
    }

}
