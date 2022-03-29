package in.hocg.boot.youtube.autoconfiguration.exception;

/**
 * Created by hocgin on 2022/3/29
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TokenUnusedException extends RuntimeException {
    public TokenUnusedException() {
        this("Token 不可用");
    }

    public TokenUnusedException(String message) {
        super(message);
    }
}
