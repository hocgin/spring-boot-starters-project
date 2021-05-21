package in.hocg.boot.message.autoconfigure.service.normal.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by hocgin on 2021/4/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class RedisMessageListener<T> implements MessageListener, InitializingBean, DisposableBean {
    @Lazy
    @Autowired
    protected RedisMessageListenerContainer listenerContainer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer serializer = RedisSerializer.java();
        this.onMessage(((T) serializer.deserialize(message.getBody())));
    }

    @Override
    public void destroy() throws Exception {
        listenerContainer.removeMessageListener(this, this.getTopic());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listenerContainer.addMessageListener(this, this.getTopic());
    }

    /**
     * 处理消息业务
     *
     * @param message 消息
     */
    public abstract void onMessage(T message);


    /**
     * 指定主题
     *
     * @return 主题
     */
    protected abstract Topic getTopic();

}
