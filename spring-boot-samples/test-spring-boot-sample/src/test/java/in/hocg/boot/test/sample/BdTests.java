package in.hocg.boot.test.sample;

import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.boot.test.sample.basic.TestMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2023/12/05
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BdTests extends AbstractSpringBootTest {
    @Autowired(required = false)
    TestMapper mapper;

    @Test
    public void test() {
        System.out.println("mapper" + mapper);
    }
}
