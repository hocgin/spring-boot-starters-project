package in.hocg.boot.cache.autoconfiguration.utils;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * @author hocgin
 */
@UtilityClass
public class CacheUtils {

    /**
     * 统一KEY
     *
     * @param prefix
     * @param values
     * @return
     */
    public static String useKey(Enum<?> prefix, String... values) {
        String joinKeys = StrUtil.join(":", values);
        return String.format("%s:%s", prefix.name(), joinKeys);
    }
}
