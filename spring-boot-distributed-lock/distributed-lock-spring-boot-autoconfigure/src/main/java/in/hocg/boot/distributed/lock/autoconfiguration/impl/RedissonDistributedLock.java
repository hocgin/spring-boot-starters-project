package in.hocg.boot.distributed.lock.autoconfiguration.impl;

import in.hocg.boot.distributed.lock.autoconfiguration.core.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class RedissonDistributedLock implements DistributedLock {
    private final RedissonClient redisson;

    @Override
    public void release(String key) {
        RLock lock = redisson.getLock(key);
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    @Override
    public boolean acquire(String key, long timeout, TimeUnit timeUnit) {
        RLock lock = redisson.getLock(key);
        if (lock.isLocked()) {
            return false;
        }
        try {
            return lock.tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
