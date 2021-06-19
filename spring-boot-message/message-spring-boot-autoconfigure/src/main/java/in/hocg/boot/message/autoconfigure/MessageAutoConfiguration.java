package in.hocg.boot.message.autoconfigure;

import in.hocg.boot.message.autoconfigure.core.TransactionalMessageListener;
import in.hocg.boot.message.autoconfigure.core.TransactionalMessageBervice;
import in.hocg.boot.message.autoconfigure.data.TransactionalAspect;
import in.hocg.boot.message.autoconfigure.jdbc.mysql.TransactionalMessageBerviceImpl;
import in.hocg.boot.message.autoconfigure.properties.MessageProperties;
import in.hocg.boot.message.autoconfigure.service.local.LocalMessageQueueBervice;
import in.hocg.boot.message.autoconfigure.service.local.LocalMessageBervice;
import in.hocg.boot.message.autoconfigure.service.normal.NoneMessageQueueBervice;
import in.hocg.boot.message.autoconfigure.service.normal.NormalMessageBervice;
import in.hocg.boot.message.autoconfigure.service.normal.redis.RedisMessageQueueBervice;
import in.hocg.boot.message.autoconfigure.service.normal.rocket.RocketMessageQueueBervice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnClass({Aspect.class})
@ConditionalOnProperty(prefix = MessageProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MessageProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MessageAutoConfiguration {
    private final MessageProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DataSource.class})
    public TransactionalMessageBervice transactionalMessageBervice(DataSource dataSource) {
        return new TransactionalMessageBerviceImpl(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalMessageListener transactionalMessageListener() {
        return new TransactionalMessageListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalAspect transactionalAspect(ApplicationEventPublisher publisher) {
        return new TransactionalAspect(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalMessageBervice localMessageBervice() {
        return new LocalMessageQueueBervice();
    }

    @Bean
    @ConditionalOnMissingBean
    public NormalMessageBervice normalMessageService() {
        MessageProperties.MessageType messageType = properties.getType();
        if (MessageProperties.MessageType.Rocket.equals(messageType)) {
            return new RocketMessageQueueBervice();
        } else if (MessageProperties.MessageType.Redis.equals(messageType)) {
            return new RedisMessageQueueBervice();
        }
        return new NoneMessageQueueBervice();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedisMessageListenerContainer.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
