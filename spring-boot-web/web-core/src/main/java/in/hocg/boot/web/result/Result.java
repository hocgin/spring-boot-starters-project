package in.hocg.boot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("响应")
@NoArgsConstructor
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "错误信息", example = "服务异常")
    private String message;
    @ApiModelProperty(value = "数据")
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
