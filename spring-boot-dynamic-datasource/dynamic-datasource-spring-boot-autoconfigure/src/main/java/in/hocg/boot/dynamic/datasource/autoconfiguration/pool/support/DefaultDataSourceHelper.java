package in.hocg.boot.dynamic.datasource.autoconfiguration.pool.support;

import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.DatasourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.properties.DynamicDataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2019-09-27.
 * email: hocgin@gmail.com
 * 默认数据源
 *
 * @author hocgin
 */
public class DefaultDataSourceHelper extends DatasourceHelper {

    public DefaultDataSourceHelper(Environment environment) {
        super(environment);
    }

    @Override
    public DataSource getMainDatasource(org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Override
    public DataSource getSecondaryDataSource(DynamicDataSourceProperties.DataSourceProperties properties) {
        return DataSourceBuilder.create()
                .url(properties.getUrl())
                .driverClassName(properties.getDriverClassName())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }
}
