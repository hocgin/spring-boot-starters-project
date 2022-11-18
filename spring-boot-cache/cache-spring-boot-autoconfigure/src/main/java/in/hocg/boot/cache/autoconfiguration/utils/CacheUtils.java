package in.hocg.boot.cache.autoconfiguration.utils;

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
     * @param value
     * @return
     */
    public static String useKey(Enum<?> prefix, String value) {
        return String.format("%s:%s", prefix.name(), value);
    }
}
