package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration;

import in.hocg.boot.mybatis.plus.autoconfiguration.MyBatisPlusAutoConfiguration;
import in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.aspect.MybatisPlusJaversAuditableRepositoryAspect;
import in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core.MyBatisPlusConnectionProvider;
import in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core.MyBatisPlusAuthorProvider;
import in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core.MyBatisPlusTransactionalJaversBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.JaversBuilderPlugin;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.RegisterJsonTypeAdaptersPlugin;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.CommitPropertiesProvider;
import org.javers.spring.auditable.EmptyPropertiesProvider;
import org.javers.spring.auditable.aspect.JaversAuditableAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableConfigurationProperties(value = {JaversSqlProperties.class})
@AutoConfigureAfter(MyBatisPlusAutoConfiguration.class)
@Import({RegisterJsonTypeAdaptersPlugin.class})
public class JaversSqlAutoConfiguration {
    private final JaversSqlProperties javersSqlProperties;

    @Autowired(required = false)
    private List<JaversBuilderPlugin> plugins = new ArrayList<>();

    @Bean(name = "JaversSqlRepositoryFromStarter")
    @ConditionalOnMissingBean
    public JaversSqlRepository javersSqlRepository(ConnectionProvider connectionProvider) {
        return SqlRepositoryBuilder
            .sqlRepository()
            .withSchema(javersSqlProperties.getSqlSchema())
            .withConnectionProvider(connectionProvider)
            .withDialect(javersSqlProperties.getDialectName())
            .withSchemaManagementEnabled(javersSqlProperties.isSqlSchemaManagementEnabled())
            .withGlobalIdCacheDisabled(javersSqlProperties.isSqlGlobalIdCacheDisabled())
            .withGlobalIdTableName(javersSqlProperties.getSqlGlobalIdTableName())
            .withCommitTableName(javersSqlProperties.getSqlCommitTableName())
            .withSnapshotTableName(javersSqlProperties.getSqlSnapshotTableName())
            .withCommitPropertyTableName(javersSqlProperties.getSqlCommitPropertyTableName())
            .build();
    }

    @Bean(name = "JaversFromStarter")
    @ConditionalOnMissingBean
    public Javers javers(JaversSqlRepository sqlRepository,
                         PlatformTransactionManager transactionManager) {
        JaversBuilder javersBuilder = MyBatisPlusTransactionalJaversBuilder
            .javers()
            .withTxManager(transactionManager)
            .registerJaversRepository(sqlRepository)
            .withProperties(javersSqlProperties);

        plugins.forEach(plugin -> plugin.beforeAssemble(javersBuilder));

        return javersBuilder.build();
    }

    @Bean(name = "MyBatisPlusAuthorProvider")
    @ConditionalOnMissingBean
    public AuthorProvider myBatisPlusAuthorProvider() {
        return new MyBatisPlusAuthorProvider();
    }

    @Bean(name = "EmptyPropertiesProvider")
    @ConditionalOnMissingBean
    public CommitPropertiesProvider commitPropertiesProvider() {
        return new EmptyPropertiesProvider();
    }

    @Bean(name = "MybatisConnectionProvider")
    @ConditionalOnMissingBean
    public ConnectionProvider mybatisConnectionProvider(DataSource dataSource) {
        return new MyBatisPlusConnectionProvider(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = JaversSqlProperties.PREFIX + ".auditableAspectEnabled", havingValue = "true", matchIfMissing = true)
    public JaversAuditableAspect javersAuditableAspect(Javers javers, AuthorProvider authorProvider,
                                                       CommitPropertiesProvider commitPropertiesProvider) {
        return new JaversAuditableAspect(javers, authorProvider, commitPropertiesProvider);
    }

    @Bean
    @ConditionalOnProperty(name = JaversSqlProperties.PREFIX + ".auditableMapperAspect", havingValue = "true", matchIfMissing = true)
    public MybatisPlusJaversAuditableRepositoryAspect javersMybatisPlusAuditableRepositoryAspect(Javers javers, AuthorProvider authorProvider,
                                                                                                 CommitPropertiesProvider commitPropertiesProvider) {
        return new MybatisPlusJaversAuditableRepositoryAspect(javers, authorProvider, commitPropertiesProvider);
    }
}
