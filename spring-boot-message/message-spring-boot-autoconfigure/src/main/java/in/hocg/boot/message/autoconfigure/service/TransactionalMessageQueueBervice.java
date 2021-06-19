package in.hocg.boot.message.autoconfigure.service;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageContext;
import in.hocg.boot.message.autoconfigure.core.message.Message;

import java.util.List;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TransactionalMessageQueueBervice<M> {
    boolean syncSend(M message);

    boolean asyncSend(M message);

    default void prepare(M message) {
        getMessageContext().add(message);
    }

    default void publish() {
        this.publish(true);
    }

    default void publish(boolean isAsync) {
        final List<M> messages = getMessageContext().getAndClear();
        if (messages.isEmpty()) {
            return;
        }

        final String messageGroupSn = getMessageGroupSn();
        for (M message : messages) {
            if (message instanceof Message) {
                ((Message) message).setGroupSn(messageGroupSn);
            }
            this.saveMessage(message);
            if (isAsync) {
                this.asyncSend(message);
            } else {
                this.asyncSend(message);
            }
        }
    }

    default void clear() {
        getMessageContext().clear();
    }

    default String getMessageGroupSn() {
        return IdUtil.fastSimpleUUID();
    }

    TransactionalMessageContext<M> getMessageContext();

    default void saveMessage(M message) {
        //
    }

}
