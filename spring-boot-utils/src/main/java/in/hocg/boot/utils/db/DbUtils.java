package in.hocg.boot.utils.db;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

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

}
