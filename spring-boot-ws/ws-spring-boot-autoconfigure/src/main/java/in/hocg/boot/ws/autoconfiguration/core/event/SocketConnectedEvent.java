package in.hocg.boot.ws.autoconfiguration.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by hocgin on 2022/6/8
 * email: hocgin@gmail.com
 * 生命周期
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class SocketConnectedEvent extends ApplicationEvent {
    /**
     * 会话
     */
    private final WebSocketSession session;

    public SocketConnectedEvent(Object source, WebSocketSession session) {
        super(source);
        this.session = session;
    }
}
