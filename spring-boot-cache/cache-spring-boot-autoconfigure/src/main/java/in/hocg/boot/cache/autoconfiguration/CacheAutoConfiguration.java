package in.hocg.boot.cache.autoconfiguration;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.autoconfiguration.properties.CacheProperties;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.cache.autoconfiguration.repository.RedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import static in.hocg.boot.cache.autoconfiguration.properties.CacheProperties.COLON;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({org.springframework.boot.autoconfigure.cache.CacheProperties.class, CacheProperties.class})
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnMissingBean(CacheManager.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration")
public class CacheAutoConfiguration {
    private final org.springframework.boot.autoconfigure.cache.CacheProperties properties;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(CacheProperties properties) {
        return builder -> {
            builder = builder.cacheDefaults(redisCacheConfiguration());
            for (CacheProperties.CacheName cacheName : properties.getNames()) {
                builder.withCacheConfiguration(cacheName.getName(), redisCacheConfiguration().entryTtl(cacheName.getTtl()));
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheRepository cacheRepository(RedisTemplate redisTemplate) {
        return new RedisRepositoryImpl(redisTemplate, getKeyPrefix() + COLON);
    }

    private String getKeyPrefix() {
        org.springframework.boot.autoconfigure.cache.CacheProperties.Redis redisProperties = properties.getRedis();
        return StrUtil.emptyToDefault(redisProperties.getKeyPrefix(), applicationName);
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        org.springframework.boot.autoconfigure.cache.CacheProperties.Redis redisProperties = properties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        config = config.computePrefixWith(cacheName -> getKeyPrefix() + COLON + cacheName + COLON);

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
