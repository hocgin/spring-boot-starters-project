package in.hocg.boot.distributed.lock.autoconfiguration;

import in.hocg.boot.distributed.lock.autoconfiguration.properties.DistributedLockProperties;
import in.hocg.boot.distributed.lock.autoconfiguration.core.DistributedLock;
import in.hocg.boot.distributed.lock.autoconfiguration.core.LockAspect;
import in.hocg.boot.distributed.lock.autoconfiguration.impl.RedissonDistributedLock;
import in.hocg.boot.distributed.lock.autoconfiguration.impl.SimpleRedisDistributedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */

@Configuration
@EnableConfigurationProperties(DistributedLockProperties.class)
@ConditionalOnClass({Aspect.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class DistributedLockAutoConfiguration {
    private final DistributedLockProperties properties;

    @Bean
    @Autowired(required = false)
    @ConditionalOnMissingBean(DistributedLock.class)
    public DistributedLock distributedLock(RedissonClient redissonClient,
                                           StringRedisTemplate stringRedisTemplate) {
        DistributedLockProperties.LockType type = properties.getType();
        switch (type) {
            case Redisson:
                return new RedissonDistributedLock(redissonClient);
            case Redis:
                return new SimpleRedisDistributedLock(stringRedisTemplate);
            default:
                throw new IllegalArgumentException("DistributedLock 类型[" + type + "]不支持");
        }
    }

    @Bean
    public LockAspect lockAspect(DistributedLock distributedLock) {
        return new LockAspect(distributedLock);
    }
}
