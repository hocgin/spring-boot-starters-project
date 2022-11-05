package in.hocg.boot.named.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface NamedCacheService {

    /**
     * 批量获取
     *
     * @param keys _
     * @return _
     */
    Map<String, Object> batchGet(Collection<String> keys);

    /**
     * 批量保存
     *
     * @param caches _
     */
    @Async
    void batchPut(Map<String, Object> caches);

    /**
     * 过期操作
     *
     * @param namedType
     * @param args
     * @param id
     */
    @Async
    void clear(String namedType, String[] args, Object id);

    /**
     * key 生成策略
     *
     * @param namedRow _
     * @return _
     */
    default String getCacheKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        Object id = namedRow.getIdValue();
        String[] args = namedRow.getArgs();
        return this.getCacheKey(namedType, args, id);
    }

    default String getCacheKey(String namedType, String[] args, Object id) {
        return getCacheKey(this.getPrefix(), namedType, args, id);
    }

    default String getCacheKey(String prefix, String namedType, String[] args, Object id) {
        return StrUtil.format("{}:NamedCache:{}:{}:{}", prefix, namedType, id, Arrays.toString(args));
    }

    default String getPrefix() {
        return this.getProperties().getCache().getPrefix();
    }

    NamedProperties getProperties();
}
