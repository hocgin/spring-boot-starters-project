package in.hocg.boot.message.service.normal.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hocgin on 2021/4/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface RedisMessageListener<T> extends MessageListener {

    @Override
    default void onMessage(Message message, byte[] pattern) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.onMessage(JSON.parseObject(message.getBody(), type));
    }

    void onMessage(T message);

}
