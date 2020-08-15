package in.hocg.boot.distributed.lock.impl;

import in.hocg.boot.distributed.lock.core.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class SimpleRedisDistributedLock implements DistributedLock {

    private final StringRedisTemplate redisTemplate;

    /**
     * 简单锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean getLock(String key) {
        if (redisTemplate.hasKey(key)) {
            return false;
        }
        redisTemplate.opsForValue().set(key, "", 30, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 移除
     *
     * @param key
     */
    @Override
    public void removeLock(String key) {
        redisTemplate.delete(key);
    }
}
