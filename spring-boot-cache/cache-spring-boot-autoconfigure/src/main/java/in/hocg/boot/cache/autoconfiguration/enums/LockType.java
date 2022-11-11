package in.hocg.boot.cache.autoconfiguration.enums;

/**
 * 锁类型
 *
 * @author hocgin
 */
public enum LockType {
    /**
     * 可重入锁
     */
    Reentrant,
    /**
     * 公平锁
     */
    Fair,
    /**
     * 联锁
     */
    Multiple,
    /**
     * 红锁
     */
    RedLock,
    /**
     * 读锁
     */
    Read,
    /**
     * 写锁
     */
    Write,
    /**
     * 自动模式
     */
    Auto
}
