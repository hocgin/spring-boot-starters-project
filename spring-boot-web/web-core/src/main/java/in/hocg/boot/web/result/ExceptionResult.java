package in.hocg.boot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("异常响应")
@EqualsAndHashCode(callSuper = true)
public class ExceptionResult<T> extends Result<T> {
    @ApiModelProperty(value = "状态码", example = "500")
    private Integer status;
    @ApiModelProperty(value = "时间戳", example = "10000000")
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
