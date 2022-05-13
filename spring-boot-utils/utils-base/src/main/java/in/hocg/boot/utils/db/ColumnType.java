package in.hocg.boot.utils.db;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ColumnType {

    public JDBCType javaToSql(Class<?> type) {
        if (type == String.class) {
            return JDBCType.VARCHAR;
        } else if (type == Integer.class) {
            return JDBCType.INTEGER;
        } else if (type == Long.class) {
            return JDBCType.BIGINT;
        } else if (type == Double.class || type == Float.class) {
            return JDBCType.DOUBLE;
        } else if (type == BigDecimal.class) {
            return JDBCType.DECIMAL;
        } else if (type == Boolean.class) {
            return JDBCType.BOOLEAN;
        } else if (type == LocalDateTime.class) {
            return JDBCType.TIMESTAMP;
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
