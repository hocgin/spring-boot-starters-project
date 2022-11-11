package in.hocg.boot.cache.autoconfiguration.lock;


import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import in.hocg.boot.utils.function.SupplierThrow;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    /**
     * 尝试获取锁
     *
     * @param lockName  锁名
     * @param lockType  锁类型
     * @param waitTime  等待时间
     * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间
     * @param timeUnit  时间参数
     * @return 是否成功
     * @throws InterruptedException
     */
    boolean tryLock(String[] lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;


    /**
     * 解锁
     *
     * @param lockName 锁名
     * @param lockType 锁类型
     */
    void unLock(String[] lockName, LockType lockType);

    /**
     * 自定获取锁后执行方法
     *
     * @param lockName  锁名
     * @param lockType  锁类型
     * @param waitTime  等待锁超时时间
     * @param leaseTime 自动解锁时间，自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放，默认100
     * @param timeUnit  时间单位
     * @param supplier  获取锁后的回调
     * @return 返回的数据
     */
    <T> T lock(SupplierThrow<T> supplier, String[] lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit);


    RLock getLock(String[] lockName, LockType lockType);
}
