package in.hocg.boot.mybatis.plus.sample.extensions.config;

import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigValueMpeMapper;
import in.hocg.boot.test.autoconfiguration.MybatisTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by hocgin on 2022/3/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@MybatisTest
class ConfigValueMapperTests {
    @Autowired(required = false)
    private ConfigValueMpeMapper mapper;


    @Test
//    @Sql(scripts = "/sql/tables/config.sql")
    void ping() {
        ConfigValue configValue = new ConfigValue();
        configValue.setRefId(1L);
        configValue.setItemId(1L);
        mapper.insert(configValue);

//        Optional<ConfigValue> value = mapper.getByScopeAndRefIdAndName("system", 1L, "test");
    }
}
