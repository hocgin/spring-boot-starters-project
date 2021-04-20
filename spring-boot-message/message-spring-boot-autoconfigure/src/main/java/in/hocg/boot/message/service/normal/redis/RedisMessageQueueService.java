package in.hocg.boot.message.service.normal.redis;

import in.hocg.boot.message.service.normal.AbsMessageQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;

/**
 * Created by hocgin on 2021/4/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class RedisMessageQueueService extends AbsMessageQueueService {
    private final RedisTemplate redisTemplate;

    @Override
    public boolean asyncSend(String destination, Message<?> message) {
        redisTemplate.convertAndSend(destination, message);
        return true;
    }

    @Override
    public boolean asyncSend(String destination, Message<?> message, long timeout) {
        redisTemplate.convertAndSend(destination, message);
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message) {
        redisTemplate.convertAndSend(destination, message);
        return false;
    }

    @Override
    public boolean syncSend(String destination, Message<?> message, long timeout) {
        redisTemplate.convertAndSend(destination, message);
        return false;
    }
}
