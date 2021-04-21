package in.hocg.boot.message.sample.message;

import in.hocg.boot.message.sample.pojo.TestMessageDto;
import in.hocg.boot.message.service.normal.redis.RedisMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2021/4/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Component
public class TestMessageListener extends RedisMessageListener<Message<TestMessageDto>> {
    public static final String TOPIC = "test";

    @Override
    public void onMessage(Message<TestMessageDto> message) {
        log.debug("消费消息: {}", message);
    }

    @Override
    protected Topic getTopic() {
        return new PatternTopic(TestMessageListener.TOPIC);
    }
}
