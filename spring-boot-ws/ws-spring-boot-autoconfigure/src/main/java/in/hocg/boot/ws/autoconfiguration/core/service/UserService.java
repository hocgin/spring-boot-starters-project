package in.hocg.boot.ws.autoconfiguration.core.service;

import java.security.Principal;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface UserService {

    Principal loadUserByTicket(String ticket);
}
