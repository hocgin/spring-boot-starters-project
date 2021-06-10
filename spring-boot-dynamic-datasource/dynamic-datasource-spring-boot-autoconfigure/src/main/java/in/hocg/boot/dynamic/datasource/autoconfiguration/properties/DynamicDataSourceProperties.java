package in.hocg.boot.dynamic.datasource.autoconfiguration.properties;

import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.DatasourceHelper;
import in.hocg.boot.dynamic.datasource.autoconfiguration.pool.DatasourceHelperManager;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {
    public static final String PREFIX = "boot.dynamic";
    /**
     * 数据源助手管理
     */
    private Class<? extends DatasourceHelper> helperClass = DatasourceHelperManager.class;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceProperties> datasource = Collections.emptyMap();

    @Data
    @Accessors(chain = true)
    public static class DataSourceProperties {
        /**
         * 数据库地址
         */
        private String url;
        /**
         * 数据库驱动
         */
        private String driverClassName;
        /**
         * 数据库用户名
         */
        private String username;
        /**
         * 数据库密码
         */
        private String password;
        /**
         * 定制化配置(boot 定制)
         */
        private Map<String, String> settings = Collections.emptyMap();
    }
}
