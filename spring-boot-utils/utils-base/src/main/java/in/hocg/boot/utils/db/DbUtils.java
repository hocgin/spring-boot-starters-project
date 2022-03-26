package in.hocg.boot.utils.db;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import in.hocg.boot.utils.LangUtils;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class DbUtils {

    public LocalDateTime asLocalDateTime(String str) {
        return LocalDateTimeUtil.parse(StrUtil.nullToEmpty(str));
    }

    public LocalDateTime getLocalDateTime(Entity entity, String columnName) {
        Object value = entity.get(columnName);
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value);
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }

        if (value instanceof String) {
            String str = (String) value;
            return LangUtils.callIfNotNull(str, s -> {
                int length = StrUtil.length(s);
                if (length == 23) {
                    return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                } else if (length == 22) {
                    return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"));
                }
                return null;
            }).orElse(null);
        }

        throw new UnsupportedOperationException();
    }

}
