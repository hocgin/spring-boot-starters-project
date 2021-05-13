package in.hocg.boot.cache.tests;

import in.hocg.boot.cache.repository.CacheRepository;
import in.hocg.boot.cache.sample.BootApplication;
import in.hocg.boot.test.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private CacheRepository cacheRepository;

    @Test
    public void mock() {
        String value = "good";
        cacheRepository.set("xx", value);

        Object value2 = cacheRepository.get("xx");

        log.info("{} :: {}", value, value2);
    }
}
