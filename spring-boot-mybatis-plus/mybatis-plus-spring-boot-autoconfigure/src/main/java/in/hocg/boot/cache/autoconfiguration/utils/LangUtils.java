package in.hocg.boot.cache.autoconfiguration.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * Created by hocgin on 2020/8/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class LangUtils {
    public boolean equals(Integer s1, Integer s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.compareTo(s2) == 0;
    }

    public boolean equals(Long s1, Long s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.compareTo(s2) == 0;
    }

    public boolean equals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.equals(s2);
    }
}
