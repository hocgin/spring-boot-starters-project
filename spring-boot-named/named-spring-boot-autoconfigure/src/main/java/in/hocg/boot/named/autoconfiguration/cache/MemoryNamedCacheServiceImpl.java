package in.hocg.boot.named.autoconfiguration.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class MemoryNamedCacheServiceImpl implements NamedCacheService, InitializingBean {
    @Getter
    private final NamedProperties properties;
    private Cache<String, Object> cachePool;

    @Override
    public Map<String, Object> batchGet(Collection<String> keys) {
        HashMap<String, Object> result = Maps.newHashMap();
        result.putAll(cachePool.getAllPresent(keys));
        return result;
    }

    @Override
    public void batchPut(Map<String, Object> caches) {
        caches.entrySet().parallelStream().forEach(entry -> cachePool.put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear(String namedType, String[] args, Object id) {
        cachePool.invalidate(this.getCacheKey(namedType, args, id));
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
