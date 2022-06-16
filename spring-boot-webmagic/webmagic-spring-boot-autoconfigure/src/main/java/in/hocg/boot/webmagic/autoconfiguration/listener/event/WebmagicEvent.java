package in.hocg.boot.webmagic.autoconfiguration.listener.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class WebmagicEvent extends ApplicationEvent {
    private final String url;

    public WebmagicEvent(String url, Object source) {
        super(source);
        this.url = url;
    }
}
