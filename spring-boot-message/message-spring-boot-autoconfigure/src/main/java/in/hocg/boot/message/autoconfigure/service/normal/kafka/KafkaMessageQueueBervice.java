package in.hocg.boot.message.autoconfigure.service.normal.kafka;

import in.hocg.boot.message.autoconfigure.service.normal.AbsMessageQueueBervice;
import org.springframework.messaging.Message;

/**
 * @author hocgin
 */
public class KafkaMessageQueueBervice extends AbsMessageQueueBervice {
    @Override
    public boolean asyncSend(String destination, Message<?> message) {
        return false;
    }

    @Override
    public boolean asyncSend(String destination, Message<?> message, long timeout) {
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message) {
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message, long timeout) {
        return false;
    }
}
