package in.hocg.boot.ws.sample;

import in.hocg.boot.ws.autoconfiguration.core.service.UserService;
import org.apache.catalina.connector.CoyotePrincipal;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class UserServiceImpl implements UserService {
    @Override
    public Principal loadUserByTicket(String ticket) {
        return new CoyotePrincipal(ticket);
    }
}
