package in.hocg.boot.mybatis.plus.sample.extensions.config;

import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigItemMpeMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hocgin on 2022/3/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
class ConfigMapperTests {
    @Autowired
    private ConfigItemMpeMapper mapper;


    @Test
    void ping() {
//        List<ConfigItem> configItems = mapper.listByScope("test");
//        log.info("ping: {}", configItems);
    }
}
