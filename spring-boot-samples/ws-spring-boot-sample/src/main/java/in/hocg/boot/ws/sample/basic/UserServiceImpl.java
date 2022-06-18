package in.hocg.boot.ws.sample.basic;

import in.hocg.boot.utils.context.security.UserPrincipal;
import in.hocg.boot.ws.autoconfiguration.core.service.SocketUserService;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class UserServiceImpl implements SocketUserService {
    @Override
    public Principal loadUserByTicket(String ticket) {
        return new UserPrincipal(ticket);
    }
}
