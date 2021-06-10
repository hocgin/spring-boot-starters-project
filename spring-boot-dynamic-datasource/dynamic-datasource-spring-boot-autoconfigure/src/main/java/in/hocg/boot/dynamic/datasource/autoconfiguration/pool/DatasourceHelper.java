package in.hocg.boot.dynamic.datasource.autoconfiguration.pool;

import com.google.common.collect.Maps;
import in.hocg.boot.dynamic.datasource.autoconfiguration.properties.DynamicDataSourceProperties;
import lombok.Getter;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by hocgin on 2019-09-27.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
public abstract class DatasourceHelper {
    private final Environment environment;

    public DatasourceHelper(Environment environment) {
        this.environment = environment;
    }

    /**
     * 获取主数据源
     *
     * @param properties 配置
     * @return
     */
    public abstract DataSource getMainDatasource(org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties);

    /**
     * 获取次数据源
     *
     * @param properties 配置
     * @return 数据源
     */
    public abstract DataSource getSecondaryDataSource(DynamicDataSourceProperties.DataSourceProperties properties);

    /**
     * 获取多从数据源
     *
     * @param properties 配置
     * @return
     */
    public Map<Object, Object> getSecondaryDataSources(Map<String, DynamicDataSourceProperties.DataSourceProperties> properties) {
        Map<Object, Object> datasourceMaps = Maps.newHashMapWithExpectedSize(properties.size());
        for (Map.Entry<String, DynamicDataSourceProperties.DataSourceProperties> entry : properties.entrySet()) {
            datasourceMaps.put(entry.getKey(), getSecondaryDataSource(entry.getValue()));
        }
        return datasourceMaps;
    }


}
