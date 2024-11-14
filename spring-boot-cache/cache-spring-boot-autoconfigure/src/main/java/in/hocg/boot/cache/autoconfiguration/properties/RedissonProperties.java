package in.hocg.boot.cache.autoconfiguration.properties;

import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Map;

/**
 * @author hocgin
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = RedissonProperties.PREFIX, ignoreUnknownFields = false)
public class RedissonProperties extends RedissonDatasourceConfig {
    public static final String PREFIX = "boot.redisson";
    public static final String MASTER_FLAG = "master";
    /**
     * 锁类型
     */
    private LockType defaultLockType = LockType.Reentrant;
    /**
     * 加锁失败后，锁尝试继续申请超时时间
     */
    private Long tryWaitTimeout = 10000L;

    private String defaultDataSource = RedissonProperties.MASTER_FLAG;

    /**
     * 连接
     */
    private Map<String, RedissonDatasourceConfig> datasource;
}
