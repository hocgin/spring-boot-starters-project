package in.hocg.boot.validation.autoconfigure.core;

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

    default boolean eq(Serializable val) {
        final Serializable code = this.getCode();
        if (code instanceof String) {
            return LangUtils.equals((String) code, (String) val);
        } else if (code instanceof Integer) {
            return LangUtils.equals((Integer) code, (Integer) val);
        }
        return false;
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

    static <T extends Enum<T>> Optional<T> of(String name, Class<T> enumClass) {
        return Optional.of(Enum.valueOf(enumClass, name));
    }
}
