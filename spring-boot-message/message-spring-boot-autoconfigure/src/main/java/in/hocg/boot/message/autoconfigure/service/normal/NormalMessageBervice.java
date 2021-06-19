package in.hocg.boot.message.autoconfigure.service.normal;

import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;
import in.hocg.boot.message.autoconfigure.service.TransactionalMessageQueueBervice;
import org.springframework.messaging.Message;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface NormalMessageBervice extends TransactionalMessageQueueBervice<TransactionalMessage> {

    boolean asyncSend(String destination, Message<?> message);

    boolean asyncSend(String destination, Message<?> message, long timeout);

    boolean syncSend(String destination, Message<?> message);

    boolean syncSend(String destination, Message<?> message, long timeout);

}
