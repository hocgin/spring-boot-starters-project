package in.hocg.boot.utils.struct.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/11/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Result<T> implements Serializable {
    private Boolean success;
    private String message;
    private T data;

    public static <T> Result<T> result(boolean success) {
        Result<T> result = new Result<>();
        return result.setSuccess(success)
            .setMessage("ok");
    }

    public static <T> Result<T> success() {
        return Result.result(true);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = Result.success();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail() {
        return Result.result(false);
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = Result.fail();
        return result.setMessage(message);
    }
}
