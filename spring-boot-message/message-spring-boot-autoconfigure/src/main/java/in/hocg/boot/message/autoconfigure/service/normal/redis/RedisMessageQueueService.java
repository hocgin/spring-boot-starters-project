package in.hocg.boot.message.autoconfigure.service.normal.redis;

import in.hocg.boot.message.autoconfigure.service.normal.AbsMessageQueueService;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.messaging.Message;

/**
 * Created by hocgin on 2021/4/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class RedisMessageQueueService extends AbsMessageQueueService {

    @Override
    public boolean asyncSend(String destination, Message<?> message) {
        this.convertAndSend(destination, message);
        return true;
    }

    @Override
    public boolean asyncSend(String destination, Message<?> message, long timeout) {
        this.convertAndSend(destination, message);
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message) {
        this.convertAndSend(destination, message);
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message, long timeout) {
        this.convertAndSend(destination, message);
        return false;
    }

    private void convertAndSend(String destination, Message<?> message) {
        byte[] rawChannel = RedisHelper.getKeySerializer().serialize(destination);
        byte[] rawMessage = RedisHelper.getValueSerializer().serialize(message);
        SpringContext.getBean(StringRedisTemplate.class).execute(connection -> {
            connection.publish(rawChannel, rawMessage);
            return null;
        }, true);
    }
}
