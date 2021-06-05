package in.hocg.boot.named;

import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.named.sample.BootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2021/6/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Tests extends AbstractSpringBootTest {

    @Test
    public void test() {
    }
}
