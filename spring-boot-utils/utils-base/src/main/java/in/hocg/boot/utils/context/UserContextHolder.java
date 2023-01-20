package in.hocg.boot.utils.context;

import in.hocg.boot.utils.exception.UnAuthenticationException;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class UserContextHolder {
    private final ThreadLocal<Long> USER_ID = ThreadLocal.withInitial(() -> null);

    public void setUserId(Long tenantId) {
        USER_ID.set(tenantId);
    }

    public Long getUserId() {
        return USER_ID.get();
    }

    public Long getUserIdThrow() {
        Long userId = getUserId();
        if (Objects.isNull(userId)) {
            throw new UnAuthenticationException();
        }
        return userId;
    }

    public void clear() {
        USER_ID.remove();
    }
}
