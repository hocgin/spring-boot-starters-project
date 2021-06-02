package in.hocg.boot.named.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
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
     * key 生成策略
     *
     * @param namedRow _
     * @return _
     */
    default String getCacheKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        Object id = namedRow.getIdValue();
        String[] args = namedRow.getArgs();
        return StrUtil.format("NamedCache:{}:{}:{}", namedType, id, Arrays.toString(args));
    }
}
