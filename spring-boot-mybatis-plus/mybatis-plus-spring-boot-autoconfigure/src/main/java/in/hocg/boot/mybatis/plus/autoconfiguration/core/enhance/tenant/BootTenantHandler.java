package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.MybatisContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.properties.MyBatisPlusProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Objects;

/**
 * Created by hocgin on 2021/12/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = MyBatisPlusProperties.Tenant.PREFIX, name = "enabled")
@EnableConfigurationProperties({MyBatisPlusProperties.class})
public class BootTenantHandler implements TenantLineHandler {
    private final MyBatisPlusProperties properties;
    private final MybatisContextHolder contextHolder;

    @Override
    public Expression getTenantId() {
        if (contextHolder.isIgnoreTenant()) {
            return new NullValue();
        }
        Long tenantId = contextHolder.getTenantId();
        if (Objects.isNull(tenantId)) {
            return new NullValue();
        }
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getTenant().getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (contextHolder.isIgnoreTenant()) {
            return true;
        }
        MyBatisPlusProperties.Tenant tenant = properties.getTenant();

        boolean hasIgnore = tenant.getIgnoreTables().stream().anyMatch((e) -> e.equalsIgnoreCase(tableName));
        boolean hasNotNeed = tenant.getNeedTables().stream().noneMatch((e) -> e.equalsIgnoreCase(tableName));

        // 需要忽略 或者 非必须
        return hasIgnore && hasNotNeed;
    }

}
