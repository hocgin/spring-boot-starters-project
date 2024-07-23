package in.hocg.boot.kafka.autoconfiguration;

import in.hocg.boot.kafka.autoconfiguration.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.BatchMessageConverter;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = KafkaProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({KafkaProperties.class, org.springframework.boot.autoconfigure.kafka.KafkaProperties.class})
//@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.kafka.KafkaAnnotationDrivenConfiguration")
public class KafkaAutoConfiguration implements InitializingBean {
    public static final String BATCH_KAFKA_LISTENER_FACTORY = "customBatchFactory";
    private final ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer;
    private final org.springframework.boot.autoconfigure.kafka.KafkaProperties originProperties;
    private final RecordMessageConverter messageConverter;
    private final BatchMessageConverter batchMessageConverter;
    private final BatchErrorHandler batchErrorHandler;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public KafkaAutoConfiguration(ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer,
                                  org.springframework.boot.autoconfigure.kafka.KafkaProperties properties,
                                  ObjectProvider<RecordMessageConverter> messageConverter,
                                  ObjectProvider<BatchMessageConverter> batchMessageConverter,
                                  ObjectProvider<BatchErrorHandler> batchErrorHandler) {
        this.kafkaListenerContainerFactoryConfigurer = kafkaListenerContainerFactoryConfigurer;
        this.originProperties = properties;
        this.messageConverter = messageConverter.getIfUnique();
        this.batchMessageConverter = batchMessageConverter.getIfUnique(() -> new BatchMessagingMessageConverter(this.messageConverter));
        this.batchErrorHandler = batchErrorHandler.getIfUnique();
    }

    public ConsumerFactory<Object, Object> kafkaConsumerFactory(org.springframework.boot.autoconfigure.kafka.KafkaProperties kafkaProperties) {
        final Map<String, Object> configs = customizerProperties(kafkaProperties.buildConsumerProperties());
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    public Map<String, Object> customizerProperties(Map<String, Object> configs) {
        configs.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return configs;
    }


    /**
     * 解决批量消费的问题
     *
     * @return 批量工厂类
     */
    @Bean("batchFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> batchFactory() {
        return newBatchFactory(0, 0, false);
    }

    /**
     * 解决批量消费的问题
     *
     * @return 批量工厂类
     */
    @Bean(BATCH_KAFKA_LISTENER_FACTORY)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> customBatchFactory() {
        return newBatchFactory(0, 0, true);
    }

    /**
     * mq消息工厂
     *
     * @param maxPollRecords 消息最大拉取数
     * @return 批量工厂类
     */
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> newBatchFactory(int maxPollRecords, int concurrency, boolean batch) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<Object, Object> consumerFactory;

        if (0 != maxPollRecords) {
            Map<String, Object> configs = customizerProperties(originProperties.buildConsumerProperties());
            configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
            consumerFactory = new DefaultKafkaConsumerFactory<>(configs);
        } else {
            consumerFactory = kafkaConsumerFactory(originProperties);
        }

        kafkaListenerContainerFactoryConfigurer.configure(factory, consumerFactory);
        if (0 != concurrency) {
            factory.setConcurrency(concurrency);
        }
        if (batch) {
            factory.setBatchListener(batch);
            factory.setBatchErrorHandler(this.batchErrorHandler);
            factory.setMessageConverter(batchMessageConverter);
        }
        return factory;
    }

}
