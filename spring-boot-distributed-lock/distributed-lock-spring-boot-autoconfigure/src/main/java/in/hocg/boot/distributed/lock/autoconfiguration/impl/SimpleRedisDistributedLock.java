package in.hocg.boot.distributed.lock.autoconfiguration.impl;

import in.hocg.boot.distributed.lock.autoconfiguration.core.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
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

    @Override
    public boolean acquire(String key, long timeout, TimeUnit timeUnit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "", timeout, timeUnit);
        return Objects.nonNull(result) && result;
    }

    @Override
    public void release(String key) {
        redisTemplate.delete(key);
    }
}
