package in.hocg.boot.dynamic.datasource.autoconfiguration.pool;

import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.support.DefaultDataSourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.support.DruidDataSourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.properties.DynamicDataSourceProperties;
import in.hocg.boot.utils.ClassUtils;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2019-09-27.
 * email: hocgin@gmail.com
 * 自动配置数据源
 * - 支持 Spring Boot 默认
 * - 支持 Druid
 *
 * @author hocgin
 */
public class DatasourceHelperManager extends DatasourceHelper {

    private DatasourceHelper dataSourceHelper;

    public DatasourceHelperManager(Environment environment) {
        super(environment);
    }

    private DatasourceHelper getDataSourceHelper() {
        if (Objects.nonNull(dataSourceHelper)) {
            return dataSourceHelper;
        }

        if (ClassUtils.hasClass(DruidDataSourceHelper.DATA_SOURCE_CLASS_NAME)) {
            dataSourceHelper = new DruidDataSourceHelper(getEnvironment());
        } else {
            dataSourceHelper = new DefaultDataSourceHelper(getEnvironment());
        }
        return dataSourceHelper;
    }

    @Override
    public DataSource getMainDatasource(org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties) {
        return getDataSourceHelper().getMainDatasource(properties);
    }

    @Override
    public DataSource getSecondaryDataSource(DynamicDataSourceProperties.DataSourceProperties properties) {
        return getDataSourceHelper().getSecondaryDataSource(properties);
    }

    @Override
    public Map<Object, Object> getSecondaryDataSources(Map<String, DynamicDataSourceProperties.DataSourceProperties> properties) {
        return getDataSourceHelper().getSecondaryDataSources(properties);
    }
}
