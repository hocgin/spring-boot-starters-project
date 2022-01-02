package in.hocg.boot.utils.context;

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

    public void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public Long getTenantId() {
        return TENANT_ID.get();
    }

    public void clear() {
        TENANT_ID.remove();
    }
}
