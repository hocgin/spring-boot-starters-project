package in.hocg.boot.message.data.client.jdbc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class JdbcSql {
    private final String sql;
    private final Object[] args;

    public JdbcSql(String sql, Object[] args) {
        this.sql = sql;
        this.args = args;
    }

    public static JdbcSql create(String sql, Object... args){
        if (Objects.isNull(args)) {
            return new JdbcSql(sql, new Object[]{});
        }
        return new JdbcSql(sql, args);
    }
}
