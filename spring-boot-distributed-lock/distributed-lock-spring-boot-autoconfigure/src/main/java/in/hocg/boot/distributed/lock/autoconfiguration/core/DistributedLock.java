package in.hocg.boot.distributed.lock.autoconfiguration.core;

import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface DistributedLock {

    /**
     * 释放锁
     *
     * @param key 锁的KEY
     */
    void release(String key);

    /**
     * 获取锁
     *
     * @param key      锁的KEY
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @return 是否成功
     */
    boolean acquire(String key, long timeout, TimeUnit timeUnit);
}
