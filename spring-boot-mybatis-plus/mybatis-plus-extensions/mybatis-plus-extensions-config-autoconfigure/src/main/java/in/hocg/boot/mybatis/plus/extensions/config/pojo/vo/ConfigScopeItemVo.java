package in.hocg.boot.mybatis.plus.extensions.config.pojo.vo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class ConfigScopeItemVo implements Serializable {
    private Long itemId;
    /**
     * 配置项编码
     */
    private String name;
    /**
     * 配置项值(可能为空)
     */
    private String value;
    /**
     * 配置项值类型
     */
    private String type;
    /**
     * 配置项值, 默认值
     */
    private String defaultValue;
    /**
     * 是否可读
     */
    private Boolean readable;
    /**
     * 是否可写
     */
    private Boolean writable;
    /**
     * 是否可空
     */
    private Boolean nullable;

    @SneakyThrows
    public <T> Optional<T> getValue() {
        Class<?> clazz = Class.forName(type);
        boolean isBasicType = ClassUtil.isBasicType(clazz);
        String valueStr = null;
        if (ObjectUtil.isNull(value)) {
            valueStr = defaultValue;
        }

        Object value = null;
        if (isBasicType) {
            value = Convert.convert(clazz, valueStr);
        }
        return Optional.ofNullable((T) value);
    }
}
