package in.hocg.boot.named.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class DefaultNamedCacheService implements NamedCacheService {
    private final Cache<String, Object> cachePool = CacheBuilder.newBuilder()
        .maximumSize(1000L)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();

    @Override
    public Object get(String key) {
        return cachePool.getIfPresent(key);
    }

    @Override
    public void put(String key, Object value) {
        cachePool.put(key, value);
    }
}
