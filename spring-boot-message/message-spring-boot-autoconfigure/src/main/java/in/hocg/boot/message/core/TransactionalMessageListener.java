package in.hocg.boot.message.core;

import in.hocg.boot.message.MessageFactory;
import in.hocg.boot.message.data.TransactionalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class TransactionalMessageListener {

    /**
     * 事务提交前
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(TransactionalEvent event) {
        log.info("==> 事务提交前 {}", event);
    }

    /**
     * 事务提交后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(TransactionalEvent event) {
        log.info("==> 事务提交后 {}", event);
    }

    /**
     * 事务回滚后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollback(TransactionalEvent event) {
        log.info("==> 事务回滚后 {}", event);
        MessageFactory.transactional().clear();
    }

    /**
     * 事务完成后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(TransactionalEvent event) {
        log.info("==> 事务完成后 {}", event);
        MessageFactory.transactional().publish();
    }

}

