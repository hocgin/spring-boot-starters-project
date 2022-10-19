package in.hocg.boot.named.autoconfiguration.cache;

import com.google.common.collect.Maps;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
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

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */

@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class RedisNamedCacheServiceImpl implements NamedCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final NamedProperties properties;
    private final RedisSerializer<String> keySerializer = RedisSerializer.string();
    private final RedisSerializer<Object> valueSerializer = RedisSerializer.java();

    @Override
    public Map<String, Object> batchGet(Collection<String> keys) {
        Map<String, Object> result = Maps.newHashMap();
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            keys.parallelStream().forEach(key -> {
                Object value = valueSerializer.deserialize(connection.get(Objects.requireNonNull(keySerializer.serialize(key))));
                if (Objects.nonNull(value)) {
                    result.put(key, value);
                }
            });
            return null;
        });
        return result;
    }

    @Override
    public void batchPut(Map<String, Object> caches) {
        NamedProperties.CacheConfig cache = properties.getCache();
        Expiration expiration = Expiration.from(cache.getExpired());
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            caches.entrySet().parallelStream().forEach(entry ->
                connection.set(Objects.requireNonNull(keySerializer.serialize(entry.getKey())),
                    Objects.requireNonNull(valueSerializer.serialize(entry.getValue())),
                    expiration, RedisStringCommands.SetOption.UPSERT));
            return null;
        });
    }
}
