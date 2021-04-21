package in.hocg.boot.message;

import in.hocg.boot.message.sample.BootApplication;
import in.hocg.boot.message.sample.message.TestMessageListener;
import in.hocg.boot.message.sample.pojo.TestMessageDto;
import in.hocg.boot.message.service.normal.NormalMessageService;
import in.hocg.boot.test.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by hocgin on 2021/4/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@ActiveProfiles("local")
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisMessageTests extends AbstractSpringBootTest {
    @Autowired
    private NormalMessageService messageService;

    @Test
    public void sendTest() {
        TestMessageDto payload = new TestMessageDto();
        payload.setBody("Hi");
        messageService.asyncSend(TestMessageListener.TOPIC, MessageBuilder.withPayload(payload).build());
    }
}
