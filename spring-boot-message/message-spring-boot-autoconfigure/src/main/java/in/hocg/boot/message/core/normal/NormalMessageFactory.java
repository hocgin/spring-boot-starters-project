package in.hocg.boot.message.core.normal;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.message.core.MessageFactory;
import in.hocg.boot.message.data.PersistenceMessage;
import in.hocg.boot.web.SpringContext;
import org.apache.logging.log4j.util.Strings;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 * - destination
 * - header
 * - payload
 *
 * @author hocgin
 */
public class NormalMessageFactory implements MessageFactory {
    public static final NormalMessageFactory ME = new NormalMessageFactory();

    private NormalMessageFactory() {
    }

    public SendResult syncSend(PersistenceMessage message) {
        final String destination = message.getDestination();
        final String headersStr = message.getHeaders();
        final MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message.getPayload());
        if (Strings.isNotBlank(headersStr)) {
            messageBuilder.copyHeaders(JSONUtil.parseObj(headersStr));
        }
        return syncSend(destination, messageBuilder.build());
    }

    public SendResult syncSend(String destination, Message<?> message) {
        return getMQTemplate().syncSend(destination, message);
    }

    public SendResult syncSend(String destination, Message<?> message, long timeout) {
        return getMQTemplate().syncSend(destination, message, timeout);
    }

    private RocketMQTemplate getMQTemplate() {
        return SpringContext.getBean(RocketMQTemplate.class);
    }
}
