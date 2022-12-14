package in.hocg.boot.kafka.sample.consumer;

import in.hocg.boot.kafka.sample.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author hocgin
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MessageConsumer {

    @KafkaListener(topics = KafkaConstant.TOPIC, groupId = KafkaConstant.GROUP)
    public void handleTopic(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        System.out.println("消费：" + record.topic() + "-" + record.partition() + "-" + record.value());

        // 消费成功确认
        ack.acknowledge();
    }

    @KafkaListener(topics = KafkaConstant.TOPIC, groupId = KafkaConstant.GROUP)
    public void handleTopic2(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        System.out.println("消费：" + record.topic() + "-" + record.partition() + "-" + record.value());

        // 跳过消息
        ack.nack(1, 100L);
    }

}
