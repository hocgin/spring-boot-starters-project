package in.hocg.boot.utils.enums;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import in.hocg.boot.utils.LangUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by hocgin on 2020/10/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ICode {

    Serializable getCode();

    default String getCodeStr() {
        return Convert.toStr(getCode());
    }

    default Integer getCodeInt() {
        return Convert.toInt(getCode());
    }

    default boolean eq(Serializable val) {
        return LangUtils.equal(this.getCode(), val);
    }

    default boolean anyMatch(ICode... codes) {
        if (ArrayUtil.isEmpty(codes)) {
            return false;
        }
        return ArrayUtil.contains(codes, this);
    }

    default boolean anyMatch(Serializable... codes) {
        if (ArrayUtil.isEmpty(codes)) {
            return false;
        }
        return ArrayUtil.contains(codes, this.getCode());
    }

    static <T extends ICode> T ofThrow(Serializable code, Class<T> enumClass) {
        final Optional<T> enumOpl = of(code, enumClass);
        if (enumOpl.isPresent()) {
            return enumOpl.get();
        } else {
            throw new IllegalArgumentException("未找到匹配的类型");
        }
    }

    static <T extends ICode> Optional<T> of(Serializable code, Class<T> enumClass) {
        final T[] constants = enumClass.getEnumConstants();
        for (T value : constants) {
            if (value.eq(code)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    static <T extends Enum<T>> Optional<T> ofName(String name, Class<T> enumClass) {
        return Optional.of(Enum.valueOf(enumClass, name));
    }
}
