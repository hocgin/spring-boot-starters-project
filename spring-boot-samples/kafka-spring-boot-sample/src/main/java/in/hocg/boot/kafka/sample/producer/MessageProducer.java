package in.hocg.boot.kafka.sample.producer;

import in.hocg.boot.kafka.sample.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hocgin
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     *
     * @param content
     */
    public void sendMessage(String content) {
        kafkaTemplate.send(KafkaConstant.TOPIC, content);
    }
}
