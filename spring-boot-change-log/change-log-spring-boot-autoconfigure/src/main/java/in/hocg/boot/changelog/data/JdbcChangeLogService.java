package in.hocg.boot.changelog.data;

import cn.hutool.core.lang.Assert;
import in.hocg.boot.changelog.core.ChangeLogService;
import in.hocg.boot.changelog.compare.ChangeLogDto;
import in.hocg.boot.changelog.compare.FieldChangeDto;
import in.hocg.boot.utils.sql.JdbcSql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

/**
 * Created by hocgin on 2021/1/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class JdbcChangeLogService implements ChangeLogService {
    private final JdbcTemplate jdbcTemplate;

    public JdbcChangeLogService(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(ChangeLogDto dto) {
        JdbcSql jdbcSql = SqlTemplate.getInsertSqlByChangeLog(dto);
        String sql = jdbcSql.getSql();
        Object[] args = jdbcSql.getArgs();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int changeRow = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                ps.setObject(i + 1, arg);
            }
            return ps;
        }, keyHolder);
        Assert.isTrue(changeRow > 0, "新增失败");
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        for (FieldChangeDto changeDto : dto.getChange()) {
            jdbcSql = SqlTemplate.getInsertSqlByFieldChange(id, changeDto);
            changeRow = jdbcTemplate.update(jdbcSql.getSql(), jdbcSql.getArgs());
            Assert.isTrue(changeRow > 0);
        }
    }


}
