package in.hocg.boot.message.autoconfigure.service.normal;

import org.springframework.messaging.Message;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class NoneMessageQueueService extends AbsMessageQueueService {
    @Override
    public boolean asyncSend(String destination, Message<?> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean asyncSend(String destination, Message<?> message, long timeout) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean syncSend(String destination, Message<?> message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean syncSend(String destination, Message<?> message, long timeout) {
        throw new UnsupportedOperationException();
    }
}
