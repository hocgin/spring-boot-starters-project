package in.hocg.boot.message.data.client.jdbc;

import in.hocg.boot.message.data.PersistenceMessage;
import in.hocg.boot.utils.sql.JdbcSql;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class JdbcSqlUtils {
    private static final String TABLE_NAME = "boot_persistence_message";

    public static JdbcSql getInsertMessageArgs(PersistenceMessage message) {
        String sql = "insert into " + TABLE_NAME + "(group_sn, headers, destination, payload, published, prepared_at, created_at) values (?,?,?,?,?,?,?)";
        return JdbcSql.create(sql, message.getGroupSn(),
            message.getHeaders(),
            message.getDestination(),
            message.getPayload(),
            PersistenceMessagePublished.Prepare.getCode(),
            message.getPreparedAt(),
            LocalDateTime.now());
    }
}
