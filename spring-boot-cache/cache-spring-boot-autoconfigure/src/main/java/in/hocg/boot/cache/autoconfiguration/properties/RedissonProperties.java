package in.hocg.boot.cache.autoconfiguration.properties;

import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import in.hocg.boot.cache.autoconfiguration.enums.RedisMode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author hocgin
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = RedissonProperties.PREFIX, ignoreUnknownFields = false)
public class RedissonProperties {
    public static final String PREFIX = "boot.redisson";
    /**
     * 锁类型
     */
    private LockType defaultLockType = LockType.Reentrant;
    /**
     * 锁超时时间
     */
    private Long lockTimeout = 30000L;
    /**
     * 加锁失败后，锁尝试超时时间
     */
    private Long tryWaitTimeout = 10000L;

    /**
     * 库
     */
    private Integer database;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 密码
     */
    private String password;

    /**
     * 模式
     */
    private RedisMode mode = RedisMode.Single;

    /**
     * 池配置
     */
    private RedisPoolProperties pool = RedisPoolProperties.DEFAULT;

    /**
     * 单机配置
     */
    private RedisSingleProperties single;

    /**
     * 集群配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;

    /**
     * 连接池配置
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class RedisPoolProperties {
        public final static RedisPoolProperties DEFAULT = new RedisPoolProperties()
            .setMaxActive(100).setMinIdle(0).setMaxIdle(8).setMaxWait(1000).setSize(100);

        private Integer maxIdle;

        private Integer minIdle;

        private Integer maxActive;

        private Integer maxWait;

        private Integer connTimeout;

        private Integer soTimeout;

        private Integer size;

    }

    /**
     * 单机模式
     */
    @Getter
    @Setter
    public static class RedisSingleProperties {
        private String address;
    }

    /**
     * 集群模式
     */
    @Getter
    @Setter
    public static class RedisClusterProperties {
        /**
         * 集群状态扫描间隔时间，单位是毫秒
         */
        private Integer scanInterval;

        /**
         * 集群节点
         */
        private String nodes;

        /**
         * 默认值： SLAVE（只在从服务节点里读取）设置读取操作选择节点的模式。 可用值为： SLAVE - 只在从服务节点里读取。
         * MASTER - 只在主服务节点里读取。 MASTER_SLAVE - 在主从服务节点里都可以读取
         */
        private String readMode;
        /**
         * （从节点连接池大小） 默认值：64
         */
        private Integer slaveConnectionPoolSize;
        /**
         * 主节点连接池大小）默认值：64
         */
        private Integer masterConnectionPoolSize;

        /**
         * （命令失败重试次数） 默认值：3
         */
        private Integer retryAttempts;

        /**
         * 命令重试发送时间间隔，单位：毫秒 默认值：1500
         */
        private Integer retryInterval;

        /**
         * 执行失败最大次数默认值：3
         */
        private Integer failedAttempts;
    }

    /**
     * 哨兵模式
     */
    @Getter
    @Setter
    public static class RedisSentinelProperties {
        /**
         * 哨兵master 名称
         */
        private String master;

        /**
         * 哨兵节点
         */
        private String nodes;

        /**
         * 哨兵配置
         */
        private Boolean masterOnlyWrite;

        private Integer failMax;
    }
}
