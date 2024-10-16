package in.hocg.boot.utils.context;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.StringPoolUtils;
import in.hocg.boot.utils.ThreadLocalClear;
import in.hocg.boot.utils.context.security.UserDetail;
import in.hocg.boot.utils.exception.UnAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

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

    public static Optional<? extends UserDetail> getUserDetail() {
        return Optional.of(USER_DETAIL.get());
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
        return (T) getUserDetail().map(UserDetail::getIgnoreTenant).orElse(true);
    }

    public static void clearAll() {
        USER_DETAIL.remove();
    }

    @Override
    public void clear() {
        UserContextHolder.clearAll();
    }

}
