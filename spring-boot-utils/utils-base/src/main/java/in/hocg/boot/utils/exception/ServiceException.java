package in.hocg.boot.utils.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * Created by hocgin on 2019-09-24.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ServiceException extends RuntimeException {
    @Getter
    private final int code;

    protected ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public static Supplier<ServiceException> supplier(CharSequence template, Object... args) {
        return () -> ServiceException.wrap(StrUtil.format(template, args));
    }

    public static ServiceException wrap(Exception e) {
        return wrap(e.getMessage());
    }

    public static ServiceException wrap(CharSequence message, Object... args) {
        return wrap(HttpStatus.HTTP_INTERNAL_ERROR, message, args);
    }

    public static ServiceException wrap(int code, CharSequence message, Object... args) {
        return new ServiceException(code, StrUtil.format(message, args));
    }

}
