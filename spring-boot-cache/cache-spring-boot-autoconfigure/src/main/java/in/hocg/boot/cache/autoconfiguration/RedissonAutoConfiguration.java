package in.hocg.boot.cache.autoconfiguration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import in.hocg.boot.cache.autoconfiguration.aspect.DistributeLockAspect;
import in.hocg.boot.cache.autoconfiguration.aspect.NoRepeatSubmitAspect;
import in.hocg.boot.cache.autoconfiguration.aspect.RateLimitAspect;
import in.hocg.boot.cache.autoconfiguration.dynamic.DynamicRoutingConnectionFactory;
import in.hocg.boot.cache.autoconfiguration.lock.DistributedLock;
import in.hocg.boot.cache.autoconfiguration.lock.RedissonDistributedLock;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonDatasourceConfig;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;
import in.hocg.boot.cache.autoconfiguration.queue.RedisDelayedQueue;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.cache.autoconfiguration.utils.RedissonUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hocgin
 */
@Configuration
@ConditionalOnClass({Redisson.class})
@AutoConfigureBefore({CacheAutoConfiguration.class, RedisAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {
    private final RedissonProperties properties;

    @Bean
    public RedisConnectionFactory dynamicRoutingConnectionFactory(RedissonProperties properties, RedisProperties redisProperties) {
        Map<String, RedissonDatasourceConfig> datasource = ObjectUtil.defaultIfNull(properties.getDatasource(), new HashMap<>(1));
        String defaultDataSource = properties.getDefaultDataSource();
        if (CollUtil.isEmpty(datasource)) {
            datasource.put(defaultDataSource, properties);
        }
        Map<String, RedissonClient> connectionFactoryMap = new HashMap<>(datasource.size());
        datasource.forEach((k, v) -> {
            connectionFactoryMap.put(k, RedissonUtils.redissonClient(v, redisProperties));
        });
        return new DynamicRoutingConnectionFactory(connectionFactoryMap).setDefaultTargetDataSource(defaultDataSource);
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DynamicRoutingConnectionFactory.class})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RateLimitAspect rateLimitAspect(DynamicRoutingConnectionFactory connectionFactory) {
        return new RateLimitAspect(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DynamicRoutingConnectionFactory.class})
    public DistributedLock distributedLock(DynamicRoutingConnectionFactory connectionFactory) {
        return new RedissonDistributedLock(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DistributedLock.class})
    public DistributeLockAspect distributedLockAspect(DistributedLock distributedLock) {
        return new DistributeLockAspect(properties, distributedLock);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({DynamicRoutingConnectionFactory.class})
    public RedisDelayedQueue delayedQueue(DynamicRoutingConnectionFactory connectionFactory) {
        return new RedisDelayedQueue(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnBean({CacheRepository.class})
    public NoRepeatSubmitAspect noRepeatSubmitAspect(CacheRepository repository) {
        return new NoRepeatSubmitAspect(repository);
    }

}
