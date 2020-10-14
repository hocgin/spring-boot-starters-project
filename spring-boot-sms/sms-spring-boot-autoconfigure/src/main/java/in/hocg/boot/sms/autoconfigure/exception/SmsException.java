package in.hocg.boot.sms.autoconfigure.exception;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class SmsException extends RuntimeException {
    public SmsException(Throwable cause) {
        super(cause);
    }
    public SmsException(String message) {
        super(message);
    }
}
