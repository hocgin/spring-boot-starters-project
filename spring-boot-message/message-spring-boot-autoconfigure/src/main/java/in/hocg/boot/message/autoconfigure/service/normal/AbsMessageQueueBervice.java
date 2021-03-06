package in.hocg.boot.message.autoconfigure.service.normal;

import cn.hutool.core.map.MapUtil;
import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageContext;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageBervice;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class AbsMessageQueueBervice implements NormalMessageBervice {
    protected static final TransactionalMessageContext<TransactionalMessage> MESSAGE_CONTEXT = new TransactionalMessageContext<>();

    @Override
    public boolean syncSend(TransactionalMessage message) {
        final String destination = message.getDestination();
        Map<String, Object> headers = message.getHeaders();
        final MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message.getPayload());
        if (MapUtil.isNotEmpty(headers)) {
            messageBuilder.copyHeaders(headers);
        }
        return syncSend(destination, messageBuilder.build());
    }

    @Override
    public boolean asyncSend(TransactionalMessage message) {
        final String destination = message.getDestination();
        Map<String, Object> headers = message.getHeaders();
        final MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message.getPayload());
        if (MapUtil.isNotEmpty(headers)) {
            messageBuilder.copyHeaders(headers);
        }
        return asyncSend(destination, messageBuilder.build());
    }

    @Override
    public void publish(boolean isAsync) {
        final List<TransactionalMessage> messages = getMessageContext().getAndClear();
        if (messages.isEmpty()) {
            return;
        }

        final String messageGroupSn = getMessageGroupSn();
        TransactionalMessageBervice messageService = SpringContext.getBean(TransactionalMessageBervice.class);
        for (TransactionalMessage message : messages) {
            message.setGroupSn(messageGroupSn);
            boolean isOk = messageService.insertMessage(message);
            if (isAsync) {
                this.asyncSend(message);
            } else {
                this.asyncSend(message);
            }
        }
    }

    @Override
    public TransactionalMessageContext<TransactionalMessage> getMessageContext() {
        return MESSAGE_CONTEXT;
    }

}
