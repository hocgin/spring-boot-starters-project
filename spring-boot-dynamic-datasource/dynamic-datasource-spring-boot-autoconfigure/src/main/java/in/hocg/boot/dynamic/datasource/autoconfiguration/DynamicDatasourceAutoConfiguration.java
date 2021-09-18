package in.hocg.boot.dynamic.datasource.autoconfiguration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import in.hocg.boot.dynamic.datasource.autoconfiguration.aspect.DynamicDatasourceAspect;
import in.hocg.boot.dynamic.datasource.autoconfiguration.core.Constants;
import in.hocg.boot.dynamic.datasource.autoconfiguration.core.DynamicDatasourceHolder;
import in.hocg.boot.dynamic.datasource.autoconfiguration.core.RoutingDataSource;
import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.DatasourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.properties.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({DynamicDataSourceProperties.class, DataSourceProperties.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class DynamicDatasourceAutoConfiguration implements EnvironmentAware {
    private final DynamicDataSourceProperties properties;
    private final DataSourceProperties datasourceProperties;
    private Environment environment;

    @Bean
    @Primary
    public DataSource dataSource() {
        // 1. 获取数据源助手
        DatasourceHelper dataSourceHelper = getDatasourceHelper();

        // 2. 加载多数据源
        Map<Object, Object> dataSources = dataSourceHelper.getSecondaryDataSources(properties.getDatasource());
        DataSource mainDatasource = dataSourceHelper.getMainDatasource(datasourceProperties);
        dataSources.put(Constants.MAIN, mainDatasource);

        // 2. 配置数据源切换规则
        RoutingDataSource dynamicDataSource = new RoutingDataSource();
        dynamicDataSource.setTargetDataSources(dataSources);
        dynamicDataSource.setDefaultTargetDataSource(mainDatasource);
        DynamicDatasourceHolder.setDatasourceNames(dataSources.keySet());
        return dynamicDataSource;
    }

    private DatasourceHelper getDatasourceHelper() {
        try {
            return properties.getHelperClass().getConstructor(Environment.class).newInstance(environment);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException("请配置正确的" + DatasourceHelper.class.getName());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDatasourceAspect aspect() {
        return new DynamicDatasourceAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
