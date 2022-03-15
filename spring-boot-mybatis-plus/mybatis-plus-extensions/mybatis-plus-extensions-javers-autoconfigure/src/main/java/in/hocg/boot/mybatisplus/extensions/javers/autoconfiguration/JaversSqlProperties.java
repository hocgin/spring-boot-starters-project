package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration;

import in.hocg.boot.mybatis.plus.autoconfiguration.properties.MyBatisPlusProperties;
import lombok.Data;
import org.javers.repository.sql.DialectName;
import org.javers.spring.JaversSpringProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = JaversSqlProperties.PREFIX)
public class JaversSqlProperties extends JaversSpringProperties {
    public static final String PREFIX = MyBatisPlusProperties.PREFIX + ".extensions.javers";
    private DialectName dialectName = DialectName.MYSQL;
    private boolean sqlSchemaManagementEnabled = true;
    private boolean sqlGlobalIdCacheDisabled = false;
    private String sqlSchema;
    private String sqlGlobalIdTableName;
    private String sqlCommitTableName;
    private String sqlSnapshotTableName;
    private String sqlCommitPropertyTableName;

    public boolean isSqlSchemaManagementEnabled() {
        return sqlSchemaManagementEnabled;
    }

    public void setSqlSchemaManagementEnabled(boolean sqlSchemaManagementEnabled) {
        this.sqlSchemaManagementEnabled = sqlSchemaManagementEnabled;
    }

    public String getSqlSchema() {
        return sqlSchema;
    }

    public void setSqlSchema(String sqlSchema) {
        this.sqlSchema = sqlSchema;
    }

    public boolean isSqlGlobalIdCacheDisabled() {
        return sqlGlobalIdCacheDisabled;
    }

    public void setSqlGlobalIdCacheDisabled(boolean sqlGlobalIdCacheDisabled) {
        this.sqlGlobalIdCacheDisabled = sqlGlobalIdCacheDisabled;
    }


    public String getSqlGlobalIdTableName() {
        return sqlGlobalIdTableName;
    }

    public void setSqlGlobalIdTableName(String sqlGlobalIdTableName) {
        this.sqlGlobalIdTableName = sqlGlobalIdTableName;
    }

    public String getSqlCommitTableName() {
        return sqlCommitTableName;
    }

    public void setSqlCommitTableName(String sqlCommitTableName) {
        this.sqlCommitTableName = sqlCommitTableName;
    }

    public String getSqlSnapshotTableName() {
        return sqlSnapshotTableName;
    }

    public void setSqlSnapshotTableName(String sqlSnapshotTableName) {
        this.sqlSnapshotTableName = sqlSnapshotTableName;
    }

    public String getSqlCommitPropertyTableName() {
        return sqlCommitPropertyTableName;
    }

    public void setSqlCommitPropertyTableName(String sqlCommitPropertyTableName) {
        this.sqlCommitPropertyTableName = sqlCommitPropertyTableName;
    }

    @Override
    protected String defaultObjectAccessHook() {
        return null;
    }
}
