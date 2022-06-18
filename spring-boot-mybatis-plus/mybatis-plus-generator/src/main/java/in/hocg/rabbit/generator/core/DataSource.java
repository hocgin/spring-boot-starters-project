package in.hocg.rabbit.generator.core;

import com.baomidou.mybatisplus.annotation.DbType;

import java.sql.Driver;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface DataSource {

    DbType getDbType();

    String getUrl();

    Class<? extends Driver> getDriverName();

    String getUsername();

    String getPassword();
}
