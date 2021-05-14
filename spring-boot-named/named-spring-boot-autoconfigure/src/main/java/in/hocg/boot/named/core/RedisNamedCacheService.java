package in.hocg.boot.named.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */

@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class RedisNamedCacheService implements NamedCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.DAYS);
    }
}
