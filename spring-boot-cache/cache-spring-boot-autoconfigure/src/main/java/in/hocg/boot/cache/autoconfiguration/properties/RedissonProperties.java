package in.hocg.boot.cache.autoconfiguration.properties;

import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import in.hocg.boot.cache.autoconfiguration.enums.RedisMode;
import lombok.Getter;
import lombok.Setter;
import org.redisson.config.ReadMode;
import org.redisson.config.SslProvider;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.net.URL;

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
     * 申请锁超时时间
     */
    private Long lockWatchdogTimeout = 30000L;
    /**
     * 加锁失败后，锁尝试继续申请超时时间
     */
    private Long tryWaitTimeout = 10000L;

    /**
     * 密码，优先级大于 spring.redis.password
     */
    private String password;
    /**
     * database，优先级大于 spring.redis.database
     */
    private Integer database;
    /**
     * 模式
     */
    private RedisMode mode = RedisMode.Single;

    /**
     * 全局配置
     */
    private GlobalProperties global;

    /**
     * 单机配置
     */
    private SingleProperties single;

    /**
     * 集群配置
     */
    private ClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private SentinelProperties sentinel;


    /**
     * 单机模式
     */
    @Getter
    @Setter
    public static class SingleProperties {
        private String address;
        private Integer subscriptionConnectionMinimumIdleSize;
        private Integer subscriptionConnectionPoolSize;
        private Long dnsMonitoringInterval;
    }

    /**
     * 集群模式
     */
    @Getter
    @Setter
    public static class ClusterProperties extends MasterSlaveProperties {
        /**
         * 集群状态扫描间隔时间，单位是毫秒
         */
        private Integer scanInterval;

        /**
         * 集群节点
         */
        private String address;
    }

    /**
     * 哨兵模式
     */
    @Getter
    @Setter
    public static class SentinelProperties extends MasterSlaveProperties {
        /**
         * 哨兵master 名称
         */
        private String master;

        /**
         * 哨兵节点
         */
        private String address;
    }

    @Getter
    @Setter
    public abstract static class MasterSlaveProperties {
        /**
         * 从节点连接池大小
         */
        private Integer slaveConnectionPoolSize;
        /**
         * 主节点连接池大小
         */
        private Integer masterConnectionPoolSize;
        /**
         * 默认值： SLAVE（只在从服务节点里读取）设置读取操作选择节点的模式。
         * 可用值为：
         * SLAVE - 只在从服务节点里读取。
         * MASTER - 只在主服务节点里读取。
         * MASTER_SLAVE - 在主从服务节点里都可以读取
         */
        private ReadMode readMode;
        private Integer masterConnectionMinimumIdleSize;
        private Integer failedSlaveCheckInterval;
        private Integer slaveConnectionMinimumIdleSize;
        private Integer failedSlaveReconnectionInterval;
        private SubscriptionMode subscriptionMode;
        private Integer subscriptionConnectionMinimumIdleSize;
        private Integer subscriptionConnectionPoolSize;
        private Long dnsMonitoringInterval;
        private String loadBalancer;
    }

    @Getter
    @Setter
    public static class GlobalProperties extends BaseProperties {
        /**
         * 编码方式
         */
        private String codec;
        /**
         * 传输模式
         */
        private TransportMode transportMode;
        private Integer threads;
        private Integer nettyThreads;
        private Boolean referenceEnabled;
        private Boolean keepPubSubOrder;
        private Boolean decodeInExecutor;
        private Boolean useScriptCache;
        private Integer minCleanUpDelay;
        private Integer maxCleanUpDelay;
    }

    @Getter
    @Setter
    public static class BaseProperties {
        /**
         * 超时时间
         */
        private Integer timeout;
        private Integer subscriptionsPerConnection;
        private Integer retryAttempts;
        private Integer retryInterval;
        private String clientName;
        private Integer connectTimeout;
        private Boolean sslEnableEndpointIdentification;
        private String sslTruststorePassword;
        private String sslKeystorePassword;
        private Integer pingConnectionInterval;
        private Boolean keepAlive;
        private Boolean tcpNoDelay;
        private SslProvider sslProvider;
        private URL sslTruststore;
        private URL sslKeystore;
        private Integer connectionMinimumIdleSize;
        private Integer idleConnectionTimeout;
    }
}
