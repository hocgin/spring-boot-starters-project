package in.hocg.boot.utils.struct.result;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ExceptionResult<T> extends Result<T> {
    private Integer status;
    private Long timestamp;
    private String exception;

    public static <T> ExceptionResult<T> fail(Integer status, String message) {
        return ExceptionResult.fail(status, message, null);
    }

    public static <T> ExceptionResult<T> fail(Integer status, Exception e) {
        return ExceptionResult.fail(status, StrUtil.emptyToDefault(e.getMessage(), "系统繁忙"), e.getClass().getName());
    }

    public static <T> ExceptionResult<T> fail(Integer status, String message, String exception) {
        ExceptionResult<T> result = new ExceptionResult<>();
        result.setSuccess(false);
        result.setStatus(status);
        result.setException(exception);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
