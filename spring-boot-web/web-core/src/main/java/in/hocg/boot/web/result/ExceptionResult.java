package in.hocg.boot.web.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@RequiredArgsConstructor
public class ExceptionResult {
    @ApiModelProperty(value = "状态码", example = "500")
    private final Integer status;
    @ApiModelProperty(value = "错误信息", example = "服务异常")
    private final String message;
    @ApiModelProperty(value = "时间戳", example = "10000000")
    private final Long timestamp;

    public static ExceptionResult create(int status, String message) {
        return new ExceptionResult(status, message, System.currentTimeMillis());
    }

}
