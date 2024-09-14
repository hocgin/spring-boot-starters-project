package in.hocg.boot.cache.autoconfiguration;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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
    @RefreshScope
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
    @ConditionalOnBean(RedisTemplate.class)
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //为jackjson注册序列化提供能力的对象
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //系列化时间格式化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //反序列化时间格式化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        //注册进入jackson
        mapper.registerModule(javaTimeModule);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setTimeZone(TimeZone.getDefault());
        RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer(mapper);
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
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

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
