package in.hocg.boot.message.autoconfigure.core;

import in.hocg.boot.message.autoconfigure.data.TransactionalEvent;
import in.hocg.boot.message.autoconfigure.service.local.LocalMessageService;
import in.hocg.boot.message.autoconfigure.service.normal.NormalMessageService;
import in.hocg.boot.web.autoconfiguration.SpringContext;
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
        if (log.isDebugEnabled()) {
            log.debug("==> 事务提交前 {}", event);
        }
        SpringContext.getBean(NormalMessageService.class).publish();
        SpringContext.getBean(LocalMessageService.class).publish();
    }

    /**
     * 事务提交后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(TransactionalEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("==> 事务提交后 {}", event);
        }
    }

    /**
     * 事务回滚后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollback(TransactionalEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("==> 事务回滚后 {}", event);
        }
        SpringContext.getBean(NormalMessageService.class).clear();
        SpringContext.getBean(LocalMessageService.class).clear();
    }

    /**
     * 事务完成后
     *
     * @param event event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(TransactionalEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("==> 事务完成后 {}", event);
        }
    }


}

