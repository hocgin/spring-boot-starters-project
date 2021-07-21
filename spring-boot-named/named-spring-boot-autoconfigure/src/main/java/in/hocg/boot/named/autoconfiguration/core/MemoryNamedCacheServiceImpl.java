package in.hocg.boot.named.autoconfiguration.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class MemoryNamedCacheServiceImpl implements NamedCacheService, InitializingBean {
    private final NamedProperties properties;
    private Cache<String, Object> cachePool = CacheBuilder.newBuilder()
        .softValues()
        .maximumSize(10000L)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    @Override
    public Map<String, Object> batchGet(Collection<String> keys) {
        HashMap<String, Object> result = Maps.newHashMap();
        keys.parallelStream().forEach(key -> {
            Object value = cachePool.getIfPresent(key);
            if (Objects.nonNull(value)) {
                result.put(key, value);
            }
        });
        return result;
    }

    @Override
    public void batchPut(Map<String, Object> caches) {
        caches.entrySet().parallelStream().forEach(entry -> cachePool.put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NamedProperties.CacheConfig cacheConfig = properties.getCache();
        Duration expired = cacheConfig.getExpired();
        cachePool = CacheBuilder.newBuilder()
            .softValues()
            .maximumSize(10000L)
            .expireAfterWrite(expired.getSeconds(), TimeUnit.SECONDS)
            .build();
    }
}
