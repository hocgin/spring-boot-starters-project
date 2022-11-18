package in.hocg.boot.cache.sample.cache;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.sample.cache.impl.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Override
    @Cacheable(cacheNames = "USE_CACHE", key = "#id", unless = "#result == null")
    public String useCache(String id) {
        return StrUtil.format("useCache_{}", id);
    }
}
