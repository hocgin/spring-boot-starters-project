package in.hocg.boot.ws;

import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.boot.ws.sample.BootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageTests extends AbstractSpringBootTest {

    @Autowired
    SimpMessagingTemplate smt;

    @Test
    public void ping() {
        smt.convertAndSend("/");
    }

}
