package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core;

import org.javers.repository.sql.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MyBatisPlusConnectionProvider implements ConnectionProvider {
    private final DataSource dataSource;

    public MyBatisPlusConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
