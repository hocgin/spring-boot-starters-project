package in.hocg.boot.named.autoconfiguration.core;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
    private final RedisSerializer<String> keySerializer = RedisSerializer.string();
    private final RedisSerializer<Object> valueSerializer = RedisSerializer.java();
    private static final Expiration EXPIRATION = Expiration.from(1, TimeUnit.DAYS);

    @Override
    public Map<String, Object> batchGet(Collection<String> keys) {
        Map<String, Object> result = Maps.newHashMap();
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            connection.openPipeline();
            keys.parallelStream().forEach(key -> {
                Object value = valueSerializer.deserialize(connection.get(Objects.requireNonNull(keySerializer.serialize(key))));
                if (Objects.nonNull(value)) {
                    result.put(key, value);
                }
            });
            connection.closePipeline();
            return null;
        });
        return result;
    }

    @Override
    public void batchPut(Map<String, Object> caches) {
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            connection.openPipeline();
            caches.entrySet().parallelStream()
                .forEach(entry -> connection.set(Objects.requireNonNull(keySerializer.serialize(entry.getKey())), Objects.requireNonNull(valueSerializer.serialize(entry.getValue())), EXPIRATION, RedisStringCommands.SetOption.UPSERT));
            connection.closePipeline();
            return null;
        });
    }
}
