package in.hocg.boot.web.autoconfiguration.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by hocgin on 2024/08/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class PreExitCodeEvent extends ApplicationEvent {
    public PreExitCodeEvent(Object source) {
        super(source);
    }
}
