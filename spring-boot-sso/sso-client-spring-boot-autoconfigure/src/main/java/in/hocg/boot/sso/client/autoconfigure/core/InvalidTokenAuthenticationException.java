package in.hocg.boot.sso.client.autoconfigure.core;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by hocgin on 2021/1/23
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class InvalidTokenAuthenticationException extends AuthenticationException {
    public InvalidTokenAuthenticationException() {
        super("token 错误");
    }

    public InvalidTokenAuthenticationException(String message) {
        super(message);
    }
}
