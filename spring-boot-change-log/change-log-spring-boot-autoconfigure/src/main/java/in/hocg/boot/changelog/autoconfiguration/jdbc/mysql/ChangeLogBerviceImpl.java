package in.hocg.boot.changelog.autoconfiguration.jdbc.mysql;

import cn.hutool.core.util.IdUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import in.hocg.boot.changelog.autoconfiguration.compare.ChangeLogDto;
import in.hocg.boot.changelog.autoconfiguration.core.ChangeLogBervice;
import in.hocg.boot.changelog.autoconfiguration.jdbc.TableChangeLog;
import in.hocg.boot.changelog.autoconfiguration.jdbc.TableFieldChangeLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class ChangeLogBerviceImpl implements ChangeLogBervice {
    private final DataSource dataSource;

    @Override
    @SneakyThrows(SQLException.class)
    public void insert(ChangeLogDto dto) {
        String logSn = IdUtil.fastSimpleUUID();
        LocalDateTime now = LocalDateTime.now();
        Db.use(dataSource).tx(db -> {
            Long changeLogId = db.insertForGeneratedKey(Entity.create(TableChangeLog.TABLE_NAME)
                .setIgnoreNull(TableChangeLog.FIELD_REF_TYPE, dto.getRefType())
                .setIgnoreNull(TableChangeLog.FIELD_REF_ID, dto.getRefId())
                .setIgnoreNull(TableChangeLog.FIELD_CHANGE_TYPE, dto.getChangeType().getCode())
                .setIgnoreNull(TableChangeLog.FIELD_LOG_SN, logSn)
                .setIgnoreNull(TableChangeLog.FIELD_CREATOR, dto.getCreator())
                .setIgnoreNull(TableChangeLog.FIELD_CREATED_AT, now));
            List<Entity> entities = dto.getChange().parallelStream().map(item -> Entity.create(TableFieldChangeLog.TABLE_NAME)
                .setIgnoreNull(TableFieldChangeLog.FIELD_FIELD_NAME, item.getFieldName())
                .setIgnoreNull(TableFieldChangeLog.FIELD_FIELD_REMARK, item.getFieldRemark())
                .setIgnoreNull(TableFieldChangeLog.FIELD_CHANGE_REMARK, item.getChangeRemark())
                .setIgnoreNull(TableFieldChangeLog.FIELD_BEFORE_VALUE, item.getBeforeValue())
                .setIgnoreNull(TableFieldChangeLog.FIELD_AFTER_VALUE, item.getAfterValue())
                .setIgnoreNull(TableFieldChangeLog.FIELD_CHANGE_LOG_ID, changeLogId)).collect(Collectors.toList());
            db.insert(entities);
        });
    }
}
