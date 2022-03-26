package in.hocg.boot.mybatis.plus.extensions.config.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ConfigHelper {

    public String getType(Object value) {
        if (ObjectUtil.isNull(value)) {
            return null;
        }

        return value.getClass().getName();
    }

    public String toValue(Object value) {
        if (ObjectUtil.isNull(value)) {
            return null;
        }
        return StrUtil.toString(value);
    }

    @SneakyThrows
    public <T> T asValue(String valueStr, String type) {
        Class<?> clazz = Class.forName(type);
        Object value = null;
        if (ClassUtil.isBasicType(clazz)) {
            value = Convert.convert(clazz, valueStr);
        }
        return (T) value;
    }
}
