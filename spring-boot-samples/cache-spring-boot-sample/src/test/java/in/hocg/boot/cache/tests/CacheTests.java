package in.hocg.boot.cache.tests;

import cn.hutool.core.lang.Assert;
import in.hocg.boot.cache.autoconfiguration.dynamic.DynamicDatasourceHolder;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.cache.sample.BootApplication;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheTests extends AbstractSpringBootTest {
    @Autowired
    CacheRepository cacheRepository;
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void mock() {
        String value = "good";
        cacheRepository.set("xx", value);

        Object value2 = cacheRepository.get("xx");

        log.info("{} :: {}", value, value2);
    }

    @org.junit.jupiter.api.Test
    public void mock2() {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().set("testpkg", "66");
                return null;
            }
        });
    }

    @org.junit.jupiter.api.Test
    public void mutiDatasource() {
        String slave = "slave";
        String value = "good" + System.currentTimeMillis();
        cacheRepository.set("xx", value);

        DynamicDatasourceHolder.push(slave);
        Object value2 = cacheRepository.get("xx");
        DynamicDatasourceHolder.clear();
        Object value3 = cacheRepository.get("xx");
        Assert.isNull(value2);
        Assert.isTrue(value3.equals(value));

        log.info("{} :: {}", value, value2);
    }
}
