package in.hocg.boot.message.autoconfigure.service.normal.rocket;

import in.hocg.boot.message.autoconfigure.service.normal.AbsMessageQueueBervice;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class RocketMessageQueueBervice extends AbsMessageQueueBervice {

    @Override
    public boolean asyncSend(String destination, Message<?> message) {
        getMessageQueueBervice().asyncSend(destination, message, null);
        return true;
    }

    @Override
    public boolean asyncSend(String destination, Message<?> message, long timeout) {
        getMessageQueueBervice().asyncSend(destination, message, null, timeout);
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message) {
        getMessageQueueBervice().syncSend(destination, message);
        return true;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message, long timeout) {
        getMessageQueueBervice().syncSend(destination, message, timeout);
        return true;
    }

    private RocketMQTemplate getMessageQueueBervice() {
        return SpringContext.getBean(RocketMQTemplate.class);
    }

}
