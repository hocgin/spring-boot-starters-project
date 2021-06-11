package in.hocg.boot.dynamic.datasource.autoconfiguration.pool.support;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Maps;
import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.DatasourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.properties.DynamicDataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2019-09-27.
 * email: hocgin@gmail.com
 * 支持 Druid
 *
 * @author hocgin
 */
public class DruidDataSourceHelper extends DatasourceHelper {
    public static final String DATA_SOURCE_CLASS_NAME = "com.alibaba.druid.pool.DruidDataSource";

    public DruidDataSourceHelper(Environment environment) {
        super(environment);
    }

    @Override
    public DataSource getMainDatasource(org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties) {
        DruidDataSource dataSource = Binder.get(getEnvironment()).bind("spring.datasource.druid", DruidDataSource.class)
            .orElse(DruidDataSourceBuilder.create().build());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        return dataSource;
    }

    @Override
    public DataSource getSecondaryDataSource(DynamicDataSourceProperties.DataSourceProperties prop) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(prop.getUrl());
        dataSource.setUsername(prop.getUsername());
        dataSource.setPassword(prop.getPassword());
        dataSource.setDriverClassName(prop.getDriverClassName());
        return dataSource;
    }

    @Override
    public Map<Object, Object> getSecondaryDataSources(Map<String, DynamicDataSourceProperties.DataSourceProperties> properties) {
        Map<Object, Object> result = Maps.newHashMap();
        Map<String, DruidDataSource> druidDataSources = Binder.get(getEnvironment()).bind(StrUtil.format("{}.datasource", DynamicDataSourceProperties.PREFIX), Bindable.mapOf(String.class, DruidDataSource.class))
            .orElse(Collections.emptyMap());

        // 重新整理
        for (Map.Entry<String, DynamicDataSourceProperties.DataSourceProperties> entry : properties.entrySet()) {
            String key = entry.getKey();
            DynamicDataSourceProperties.DataSourceProperties prop = entry.getValue();

            DruidDataSource dataSource = druidDataSources.get(key);
            dataSource.setUrl(prop.getUrl());
            dataSource.setUsername(prop.getUsername());
            dataSource.setPassword(prop.getPassword());
            dataSource.setDriverClassName(prop.getDriverClassName());
            result.put(key, dataSource);
        }
        return result;
    }
}
