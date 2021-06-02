package in.hocg.boot.named.autoconfiguration.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

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
public class MemoryNamedCacheService implements NamedCacheService {
    private final Cache<String, Object> cachePool = CacheBuilder.newBuilder()
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
}
