package in.hocg.boot.sso.client.autoconfigure.core;

import org.springframework.security.core.Authentication;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface BearerTokenAuthentication {

    Authentication authentication(String token);
}
