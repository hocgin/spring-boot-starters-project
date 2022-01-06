package in.hocg.boot.task.autoconfiguration.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import in.hocg.boot.utils.LangUtils;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TaskUtils {

    public LocalDateTime getLocalDateTime(Entity entity, String columnName) {
        String str = entity.getStr(columnName);
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
}
