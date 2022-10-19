package in.hocg.boot.named.autoconfiguration;

import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.named.autoconfiguration.aspect.NamedAspect;
import in.hocg.boot.named.autoconfiguration.cache.MemoryNamedCacheServiceImpl;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.cache.RedisNamedCacheServiceImpl;
import in.hocg.boot.named.autoconfiguration.core.convert.IPageNamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.core.convert.IScrollNamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.core.convert.NamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.core.convert.OptionalNamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

/**
 * Created by hocgin on 2020/8/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableAsync
@ConditionalOnProperty(prefix = NamedProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NamedProperties.class)
@ConditionalOnClass({Aspect.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NamedAutoConfiguration {
    private final NamedProperties properties;

    @Bean
    public NamedAspect namedAspect(ApplicationContext context, List<NamedRowsConvert> converts) {
        return new NamedAspect(context, converts, properties);
    }

    @Bean
    @Order
    @ConditionalOnMissingBean
    public NamedCacheService memoryNamedCacheService() {
        return new MemoryNamedCacheServiceImpl(properties);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisNamedConfiguration {

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @ConditionalOnMissingBean
        @ConditionalOnBean({RedisTemplate.class, NamedProperties.class})
        public NamedCacheService redisNamedCacheService(RedisTemplate redisTemplate, NamedProperties properties) {
            return new RedisNamedCacheServiceImpl(redisTemplate, properties);
        }
    }

    @ConditionalOnClass(IPage.class)
    @Configuration(proxyBeanMethods = false)
    public static class IPageNamedRowsLoading {

        @Bean
        @ConditionalOnMissingBean(IPageNamedRowsConvert.class)
        public IPageNamedRowsConvert iPageNamedRowsConvert() {
            return new IPageNamedRowsConvert();
        }
    }

    @ConditionalOnClass(IScroll.class)
    @Configuration(proxyBeanMethods = false)
    public static class IScrollNamedRowsLoading {
        @Bean
        @ConditionalOnMissingBean(IScrollNamedRowsConvert.class)
        public IScrollNamedRowsConvert iScrollNamedRowsConvert() {
            return new IScrollNamedRowsConvert();
        }
    }

    @Bean
    @ConditionalOnMissingBean(OptionalNamedRowsConvert.class)
    public OptionalNamedRowsConvert optionalNamedRowsConvert() {
        return new OptionalNamedRowsConvert();
    }
}
