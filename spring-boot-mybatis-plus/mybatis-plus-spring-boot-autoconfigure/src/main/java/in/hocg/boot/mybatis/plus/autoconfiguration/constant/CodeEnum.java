package in.hocg.boot.mybatis.plus.autoconfiguration.constant;

import in.hocg.boot.validation.autoconfigure.core.ICode;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by hocgin on 2020/2/16.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface CodeEnum extends ICode {

    static <T extends CodeEnum> T ofThrow(Serializable code, Class<T> enumClass) {
        final Optional<T> enumOpl = of(code, enumClass);
        if (enumOpl.isPresent()) {
            return enumOpl.get();
        } else {
            throw new IllegalArgumentException("未找到匹配的类型");
        }
    }

    static <T extends CodeEnum> Optional<T> of(Serializable code, Class<T> enumClass) {
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
