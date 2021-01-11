package in.hocg.boot.message.data.client;

import in.hocg.boot.message.core.transactional.TransactionalMessage;
import in.hocg.boot.message.core.transactional.TransactionalMessageService;
import in.hocg.boot.message.data.client.jdbc.JdbcSqlUtils;
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
public class JdbcTransactionalMessageService implements TransactionalMessageService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionalMessageService(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean insertMessage(TransactionalMessage message) {
        JdbcSql jdbcSql = JdbcSqlUtils.getInsertMessageArgs(message);
        int changeRow = jdbcTemplate.update(jdbcSql.getSql(), jdbcSql.getArgs());
        return changeRow > 0;
    }

}
