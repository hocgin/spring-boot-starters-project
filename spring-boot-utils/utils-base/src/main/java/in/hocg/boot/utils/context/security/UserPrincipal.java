package in.hocg.boot.utils.context.security;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by hocgin on 2022/1/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */

@NoArgsConstructor
public class UserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;

    public UserPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
