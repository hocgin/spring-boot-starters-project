package in.hocg.boot.utils.context;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.StringPoolUtils;
import in.hocg.boot.utils.exception.UnAuthenticationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class UserContextHolder {
    private final ThreadLocal<Long> USER_ID = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<String> TRACK_ID = ThreadLocal.withInitial(() -> null);

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

    public void setTrackId(String trackId) {
        if (StrUtil.isBlank(trackId)) {
            trackId = IdUtil.fastUUID();
        }
        TRACK_ID.set(trackId);

        try {
            MDC.put(StringPoolUtils.TRACK_ID, trackId);
        } catch (Exception e) {
            log.warn("MDC.put error", e);
        }
    }

    public void clearTrackId() {
        TRACK_ID.remove();
        try {
            MDC.remove(StringPoolUtils.TRACK_ID);
        } catch (Exception e) {
            log.warn("MDC.put error", e);
        }
    }


    public String getTrackId() {
        return TRACK_ID.get();
    }
}
