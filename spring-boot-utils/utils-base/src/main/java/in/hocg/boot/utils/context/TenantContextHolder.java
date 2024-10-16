package in.hocg.boot.utils.context;

import in.hocg.boot.utils.ThreadLocalClear;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TenantContextHolder {
    private final ThreadLocal<Long> TENANT_ID = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<Boolean> IGNORE_TENANT_ID = ThreadLocal.withInitial(() -> false);

    public void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public Long getTenantId() {
        return TENANT_ID.get();
    }

    public void setIgnoreTenant(Boolean ignoreTenant) {
        IGNORE_TENANT_ID.set(ignoreTenant);
    }

    public Boolean isIgnoreTenant() {
        return IGNORE_TENANT_ID.get();
    }

    public void clear() {
        TENANT_ID.remove();
        IGNORE_TENANT_ID.remove();
    }
}
