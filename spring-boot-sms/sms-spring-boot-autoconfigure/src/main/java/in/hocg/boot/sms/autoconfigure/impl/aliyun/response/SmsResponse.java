package in.hocg.boot.sms.autoconfigure.impl.aliyun.response;

import lombok.Data;

/**
 * Created by hocgin on 2020/10/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class SmsResponse {
    private String Message;
    private String RequestId;
    private String BizId;
    private String Code;

    public boolean isSuccess() {
        return "OK".equalsIgnoreCase(Code);
    }
}
