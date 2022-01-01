package in.hocg.boot.mybatis.plus.autoconfiguration.core.context;

import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
@Deprecated
public class UserContextHolder {
    private final ThreadLocal<Long> USER_ID = ThreadLocal.withInitial(() -> null);

    public void setUserId(Long tenantId) {
        USER_ID.set(tenantId);
    }

    public Long getUserId() {
        return USER_ID.get();
    }

    public void clear() {
        USER_ID.remove();
    }
}
