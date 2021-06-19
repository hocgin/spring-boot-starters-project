package in.hocg.boot.message.autoconfigure.jdbc.mysql;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageBervice;
import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;
import in.hocg.boot.message.autoconfigure.jdbc.TablePersistenceMessage;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class TransactionalMessageBerviceImpl implements TransactionalMessageBervice {
    private final DataSource dataSource;

    @Override
    @SneakyThrows(SQLException.class)
    public boolean insertMessage(TransactionalMessage message) {
        LocalDateTime now = LocalDateTime.now();
        int row = Db.use(dataSource).insert(Entity.create(TablePersistenceMessage.TABLE_NAME)
            .setIgnoreNull(TablePersistenceMessage.FIELD_PREPARED_AT, message.getPreparedAt())
            .setIgnoreNull(TablePersistenceMessage.FIELD_PUBLISHED, TablePersistenceMessage.PersistenceMessagePublished.Prepare.getCode())
            .setIgnoreNull(TablePersistenceMessage.FIELD_PAYLOAD, message.getPayload())
            .setIgnoreNull(TablePersistenceMessage.FIELD_DESTINATION, message.getDestination())
            .setIgnoreNull(TablePersistenceMessage.FIELD_HEADERS, LangUtils.callIfNotNull(message.getHeaders(), JSONUtil::toJsonStr).orElse(null))
            .setIgnoreNull(TablePersistenceMessage.FIELD_GROUP_SN, message.getGroupSn())
            .setIgnoreNull(TablePersistenceMessage.FIELD_CREATED_AT, now));
        return row >= 0;
    }
}
