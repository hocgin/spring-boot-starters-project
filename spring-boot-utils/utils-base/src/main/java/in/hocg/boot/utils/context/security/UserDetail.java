package in.hocg.boot.utils.context.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/9/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Accessors(chain = true)
public class UserDetail extends UserPrincipal {
    @Getter
    private Serializable id;
    @Getter
    private Serializable tenantId;
    @Getter
    private Boolean ignoreTenant;
    @Getter
    private String traceId;

    public UserDetail(String name) {
        super(name);
    }

    public UserDetail(Serializable id, String username) {
        this(id, username, null, null, true);
    }

    public UserDetail(Serializable id, String name, String traceId, Serializable tenantId, Boolean ignoreTenant) {
        super(name);
        this.id = id;
        this.traceId = traceId;
        this.tenantId = tenantId;
        this.ignoreTenant = ignoreTenant;
    }
}
