package in.hocg.boot.ws.autoconfiguration.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.CloseStatus;
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
public class SocketClosedEvent extends ApplicationEvent {
    /**
     * 会话
     */
    private final WebSocketSession session;
    /**
     * 关闭原因
     */
    private final CloseStatus closeStatus;

    public SocketClosedEvent(Object source, WebSocketSession session, CloseStatus closeStatus) {
        super(source);
        this.session = session;
        this.closeStatus = closeStatus;
    }
}
