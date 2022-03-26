package in.hocg.boot.utils.struct.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2018/12/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {
    SUCCESS(200, "ok"),
    PARAMS_ERROR(501, "参数校验失败"),
    SERVICE_ERROR(502, "系统繁忙，请稍后"),
    AUTHENTICATION_ERROR(503, "拒绝访问"),
    ACCESS_DENIED_ERROR(504, "无权访问"),
    ERROR(500, "error");
    private final Integer code;
    private final String message;
}
