package in.hocg.boot.message.autoconfigure.data.client;

import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageService;
import in.hocg.boot.message.autoconfigure.data.client.jdbc.JdbcSqlUtils;
import in.hocg.boot.message.autoconfigure.utils.MessageConvert;
import in.hocg.boot.utils.sql.JdbcSql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class JdbcTransactionalMessageServiceImpl implements TransactionalMessageService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionalMessageServiceImpl(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean insertMessage(TransactionalMessage message) {
        JdbcSql jdbcSql = JdbcSqlUtils.getInsertMessageArgs(MessageConvert.asPersistenceMessage(message));
        int changeRow = jdbcTemplate.update(jdbcSql.getSql(), jdbcSql.getArgs());
        return changeRow > 0;
    }

}
