package in.hocg.boot.message.autoconfigure.service.local;

import in.hocg.boot.message.autoconfigure.core.TransactionalMessageContext;
import in.hocg.boot.web.autoconfiguration.SpringContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class LocalMessageQueueBervice implements LocalMessageBervice {
    protected static final TransactionalMessageContext<Object> MESSAGE_CONTEXT = new TransactionalMessageContext<>();

    @Override
    public boolean syncSend(Object message) {
        SpringContext.getApplicationContext().publishEvent(message);
        return true;
    }

    @Override
    public boolean asyncSend(Object message) {
        try {
            CompletableFuture.runAsync(() -> this.syncSend(message)).get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return true;
    }

    @Override
    public TransactionalMessageContext<Object> getMessageContext() {
        return MESSAGE_CONTEXT;
    }
}
