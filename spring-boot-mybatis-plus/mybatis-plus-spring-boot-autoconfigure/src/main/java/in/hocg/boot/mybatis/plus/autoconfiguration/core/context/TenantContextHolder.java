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
public class TenantContextHolder {
    private final ThreadLocal<Long> TENANT = ThreadLocal.withInitial(() -> null);

    public void setTenantId(Long tenantId) {
        TENANT.set(tenantId);
    }

    public Long getTenantId() {
        return TENANT.get();
    }

    public void clear() {
        TENANT.remove();
    }
}
