package in.hocg.boot.message.sample.service;

import in.hocg.boot.message.MessageFactory;
import in.hocg.boot.message.core.message.TransactionalMessage;
import in.hocg.boot.message.sample.controller.TransactionalController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TransactionalService {
    String topic = TransactionalController.class.getName();

    @Transactional(rollbackFor = Exception.class)
    public void transactionalPrepare() {
        final TransactionalMessage message = new TransactionalMessage();
        message.setDestination(topic);
        message.setPayload("XXX");
        MessageFactory.normal().prepare(message);

        message.setDestination(topic);
        message.setPayload("XXX");
        MessageFactory.normal().prepare(message);
//        publisher.publishEvent(new TransactionalEvent());
    }

    public void transactionalPrepare2() {
        final TransactionalMessage message = new TransactionalMessage();
        message.setDestination(topic);
        message.setPayload("XXX");
        MessageFactory.normal().prepare(message);
    }
}
