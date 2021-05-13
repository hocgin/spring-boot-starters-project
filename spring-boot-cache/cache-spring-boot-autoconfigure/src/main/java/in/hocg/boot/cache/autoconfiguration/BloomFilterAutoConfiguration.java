package in.hocg.boot.cache.autoconfiguration;

import in.hocg.boot.cache.bloom.RedisBloomFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableConfigurationProperties({BloomFilterProperties.class})
public class BloomFilterAutoConfiguration {
    private final BloomFilterProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RedisBloomFilter bloomFilter(StringRedisTemplate redisTemplate) {
        return new RedisBloomFilter(properties.getExpectedInsertions(), properties.getFpp(),
            properties.getName(), redisTemplate);
    }
}
