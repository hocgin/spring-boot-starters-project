package in.hocg.boot.distributed.lock.impl;

import in.hocg.boot.distributed.lock.core.DistributedLock;
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
    public void removeLock(String key) {
        RLock lock = redisson.getLock(key);
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    @Override
    public boolean getLock(String key) {
        RLock lock = redisson.getLock(key);
        if (lock.isLocked()) {
            return false;
        }
        try {
            return lock.tryLock(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
