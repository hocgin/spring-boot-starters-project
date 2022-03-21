package in.hocg.boot.utils.struct.result;

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

    public static <T> ExceptionResult<T> fail(int status, String message) {
        ExceptionResult<T> result = new ExceptionResult<>();
        result.setSuccess(false);
        result.setStatus(status);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
