package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core;

import lombok.extern.slf4j.Slf4j;
import org.javers.common.validation.Validate;
import org.javers.core.Javers;
import org.javers.core.commit.Commit;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.spring.transactions.JaversTransactionalDecorator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class MyBatisPlusJaversTransactionalDecorator extends JaversTransactionalDecorator implements InitializingBean {
    private final JaversSqlRepository javersSqlRepository;
    private final PlatformTransactionManager txManager;

    MyBatisPlusJaversTransactionalDecorator(Javers delegate, JaversSqlRepository javersSqlRepository, PlatformTransactionManager txManager) {
        super(delegate);
        Validate.argumentsAreNotNull(javersSqlRepository, txManager);
        this.javersSqlRepository = javersSqlRepository;
        this.txManager = txManager;
    }


    @Override
    @Transactional
    public Commit commit(String author, Object currentVersion) {
        this.registerRollbackListener();
        return super.commit(author, currentVersion);
    }

    @Override
    @Transactional
    public Commit commit(String author, Object currentVersion, Map<String, String> commitProperties) {
        this.registerRollbackListener();
        return super.commit(author, currentVersion, commitProperties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.ensureSchema();
    }

    private void ensureSchema() {
        if (this.javersSqlRepository.getConfiguration().isSchemaManagementEnabled()) {
            TransactionTemplate tmpl = new TransactionTemplate(this.txManager);
            tmpl.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    MyBatisPlusJaversTransactionalDecorator.this.javersSqlRepository.ensureSchema();
                }
            });
        }

    }

    private void registerRollbackListener() {
        if (!this.javersSqlRepository.getConfiguration().isGlobalIdCacheDisabled()) {
            if (TransactionSynchronizationManager.isSynchronizationActive() && TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        if (1 == status) {
                            MyBatisPlusJaversTransactionalDecorator.log.info("evicting javersSqlRepository local cache due to transaction rollback");
                            MyBatisPlusJaversTransactionalDecorator.this.javersSqlRepository.evictCache();
                        }
                    }
                });
            }

        }
    }
}
