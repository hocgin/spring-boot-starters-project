package in.hocg.boot.changelog.autoconfiguration.data;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.changelog.autoconfiguration.compare.ChangeLogDto;
import in.hocg.boot.changelog.autoconfiguration.compare.FieldChangeDto;
import in.hocg.boot.utils.sql.JdbcSql;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/1/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class SqlTemplate {

    public JdbcSql getInsertSqlByChangeLog(ChangeLogDto dto) {
        String logSn = IdUtil.fastSimpleUUID();
        String sql = "INSERT INTO com_change_log(log_sn, ref_type, ref_id, change_type, created_at, creator) VALUES(?,?,?,?,?,?)";
        return JdbcSql.create(sql, logSn, dto.getRefType(),
            dto.getRefId(), dto.getChangeType().getCode(), dto.getCreatedAt(), dto.getCreator());
    }

    public JdbcSql getInsertSqlByFieldChange(Long changeLogId, FieldChangeDto dto) {
        String sql = "INSERT INTO com_field_change(change_log_id, field_name, field_remark, change_remark, before_value, after_value) VALUES(?,?,?,?,?,?)";
        return JdbcSql.create(sql, changeLogId, dto.getFieldName(),
            dto.getFieldRemark(), dto.getChangeRemark(), dto.getBeforeValue(), dto.getAfterValue());
    }

}
