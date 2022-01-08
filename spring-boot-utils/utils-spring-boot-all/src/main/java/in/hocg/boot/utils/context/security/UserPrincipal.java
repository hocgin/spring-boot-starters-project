package in.hocg.boot.utils.context.security;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by hocgin on 2022/1/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class UserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 1L;
    protected final String name;

    @Override
    public String getName() {
        return name;
    }
}
