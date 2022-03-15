package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core;

import org.javers.common.exception.JaversException;
import org.javers.common.exception.JaversExceptionCode;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.sql.JaversSqlRepository;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MyBatisPlusTransactionalJaversBuilder extends JaversBuilder {
    private PlatformTransactionManager txManager;

    private MyBatisPlusTransactionalJaversBuilder() {
    }

    public static MyBatisPlusTransactionalJaversBuilder javers() {
        return new MyBatisPlusTransactionalJaversBuilder();
    }

    public MyBatisPlusTransactionalJaversBuilder withTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
        return this;
    }

    @Override
    public Javers build() {
        if (this.txManager == null) {
            throw new JaversException(JaversExceptionCode.TRANSACTION_MANAGER_NOT_SET);
        } else {
            Javers javersCore = super.assembleJaversInstance();
            return new MyBatisPlusJaversTransactionalDecorator(javersCore, this.getContainerComponent(JaversSqlRepository.class), this.txManager);
        }
    }
}
