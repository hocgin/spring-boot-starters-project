package in.hocg.boot.cache.autoconfiguration.enums;

/**
 * redis 模式
 *
 * @author hocgin
 */
public enum RedisMode {
    /**
     * 单例
     */
    Single,
    /**
     * 哨兵
     */
    Sentinel,
    /**
     * 集群
     */
    Cluster;
}
