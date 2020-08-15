package in.hocg.boot.web.result;

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
    private final Integer status;
    private final String message;
    private final Long timestamp;

    public static ExceptionResult create(int status, String message) {
        return new ExceptionResult(status, message, System.currentTimeMillis());
    }

}
