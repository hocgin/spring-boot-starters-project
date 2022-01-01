package in.hocg.boot.mybatis.plus.autoconfiguration.properties;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.ColumnConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/5/29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = MyBatisPlusProperties.PREFIX)
public class MyBatisPlusProperties {
    public static final String PREFIX = "boot.mybatis-plus";
    /**
     * 租户配置
     */
    private Tenant tenant = Tenant.DEFAULT;


    @Data
    public static class Tenant {
        public static final Tenant DEFAULT = new Tenant();
        public static final String PREFIX = MyBatisPlusProperties.PREFIX + ".tenant";
        /**
         * 是否开启租户功能
         */
        private Boolean enable = Boolean.FALSE;
        /**
         * 租户字段
         */
        private String column = ColumnConstants.TENANT_ID;
        /**
         * 要忽略租户的表
         */
        private List<String> ignoreTables = Collections.emptyList();
        /**
         * 必须使用租户的表
         */
        private List<String> needTables = Collections.emptyList();
    }


}
