package in.hocg.boot.utils.context;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.utils.ThreadLocalClear;
import in.hocg.boot.utils.context.security.UserDetail;
import in.hocg.boot.utils.exception.UnAuthenticationException;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class UserContextHolder implements ThreadLocalClear {
    private static final ThreadLocal<UserDetail> USER_DETAIL = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Boolean> IGNORE_TENANT = ThreadLocal.withInitial(() -> null);

    public static Boolean getIgnoreTenantOrDefault(Boolean ignore) {
        return getUserDetail().map(UserDetail::getIgnoreTenant).or(() -> Optional.ofNullable(IGNORE_TENANT.get())).orElse(ignore);
    }

    public static void setIgnoreTenant(Boolean ignore) {
        IGNORE_TENANT.set(ignore);
    }

    public static Optional<? extends UserDetail> getUserDetail() {
        return Optional.ofNullable(USER_DETAIL.get());
    }

    public static <T extends UserDetail> void setUserDetail(T userDetail) {
        USER_DETAIL.set(userDetail);
    }

    public static <T extends Serializable> T getUserId() {
        return (T) getUserDetail().map(UserDetail::getId).orElse(null);
    }

    public static <T extends Serializable> T getUsername() {
        return (T) getUserDetail().map(UserDetail::getName).orElse(null);
    }

    public static <T extends Serializable> T getTraceId() {
        return (T) getUserDetail().map(UserDetail::getTraceId).orElse(null);
    }

    public static <T extends Serializable> T getUserIdThrow() {
        T userId = getUserId();
        if (Objects.isNull(userId)) {
            throw new UnAuthenticationException("未登陆");
        }
        return userId;
    }

    public static <T extends Serializable> T getTenantIdThrow() {
        return (T) getUserDetail().map(UserDetail::getTenantId).orElseThrow(UnAuthenticationException::new);
    }

    public static <T extends Serializable> T getTenantId() {
        return (T) getUserDetail().map(UserDetail::getTenantId).orElse(null);
    }

    public static <T extends Serializable> T getIgnoreTenant() {
        return (T) UserContextHolder.getIgnoreTenantOrDefault(false);
    }

    public static void clearAll() {
        USER_DETAIL.remove();
        IGNORE_TENANT.remove();
    }

    @Override
    public void clear() {
        UserContextHolder.clearAll();
    }

}
