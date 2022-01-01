package in.hocg.boot.web.sample;

import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by hocgin on 2021/12/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class BaseDbTest<T> extends AbstractSpringBootTest implements InitializingBean {
    protected Class<T> mapper;
    protected JdbcTemplate jdbcTemplate;
    @Lazy
    @Autowired
    protected DataSource dataSource;
    @Lazy
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;

    protected SqlSession sqlSession(@Nullable ExecutorType type) {
        return sqlSessionFactory.openSession(type);
    }

    protected void doTest(Consumer<T> consumer) {
        try (SqlSession sqlSession = sqlSession(null)) {
            doTest(sqlSession, consumer);
        }
    }

    protected void doTestAutoCommit(Consumer<T> consumer) {
        try (SqlSession sqlSession = sqlSession(null)) {
            doTestAutoCommit(sqlSession, consumer);
        }
    }

    protected void doTest(SqlSession sqlSession, Consumer<T> consumer) {
        doMapper(sqlSession, false, consumer);
    }

    protected void doTestAutoCommit(SqlSession sqlSession, Consumer<T> consumer) {
        doMapper(sqlSession, true, consumer);
    }

    protected void doMapper(SqlSession sqlSession, boolean commit, Consumer<T> consumer) {
        T t = sqlSession.getMapper(mapper);
        consumer.accept(t);
        if (commit) {
            sqlSession.commit();
        } else {
            sqlSession.rollback();
        }
    }

    protected List<String> tableSql() {
        return null;
    }

    protected String tableDataSql() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> tableSql = tableSql();
        String tableDataSql = tableDataSql();
        mapper = (Class<T>) ClassUtil.getTypeArgument(getClass(), 0);

        jdbcTemplate = new JdbcTemplate(dataSource);
        if (CollectionUtils.isNotEmpty(tableSql)) {
            for (String sql : tableSql) {
                if (StringUtils.isNotBlank(sql)) {
                    jdbcTemplate.execute(sql);
                }
            }
        }

        if (StringUtils.isNotBlank(tableDataSql)) {
            jdbcTemplate.execute(tableDataSql);
        }
    }
}
