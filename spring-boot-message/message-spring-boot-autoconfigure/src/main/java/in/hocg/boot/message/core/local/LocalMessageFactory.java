package in.hocg.boot.message.core.local;


import in.hocg.boot.message.core.MessageFactory;
import in.hocg.boot.web.SpringContext;

/**
 * Created by hocgin on 2020/3/8.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class LocalMessageFactory implements MessageFactory {
    public static final LocalMessageFactory ME = new LocalMessageFactory();

    private LocalMessageFactory() {
    }

    public void publish(Object event) {
        this.publishEvent(event);
    }

    private void publishEvent(Object event) {
        SpringContext.getApplicationContext().publishEvent(event);
    }
}
