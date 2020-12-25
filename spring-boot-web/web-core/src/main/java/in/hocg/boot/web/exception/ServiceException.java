package in.hocg.boot.web.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import lombok.Getter;

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

    public static ServiceException wrap(Exception e) {
        return wrap(e.getMessage());
    }

    public static ServiceException wrap(String message, Object... args) {
        return wrap(HttpStatus.HTTP_INTERNAL_ERROR, message, args);
    }

    public static ServiceException wrap(int code, String message, Object... args) {
        return new ServiceException(code, StrUtil.format(message, args));
    }

}
