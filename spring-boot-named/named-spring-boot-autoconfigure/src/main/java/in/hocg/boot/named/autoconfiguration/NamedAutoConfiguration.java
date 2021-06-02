package in.hocg.boot.named.autoconfiguration;

import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.named.autoconfiguration.aspect.NamedAspect;
import in.hocg.boot.named.autoconfiguration.core.MemoryNamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.RedisNamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.convert.IPageNamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.core.convert.NamedRowsConvert;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@ConditionalOnClass({Aspect.class})
public class NamedAutoConfiguration {

    @Bean
    public NamedAspect namedAspect(ApplicationContext context, List<NamedRowsConvert> converts) {
        return new NamedAspect(context, converts);
    }

    @Bean
    @Order
    @ConditionalOnMissingBean
    public NamedCacheService memoryNamedCacheService() {
        return new MemoryNamedCacheService();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RedisTemplate.class)
    public static class RedisNamedConfiguration {
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @ConditionalOnMissingBean
        @ConditionalOnBean(RedisTemplate.class)
        public NamedCacheService redisNamedCacheService(RedisTemplate redisTemplate) {
            return new RedisNamedCacheService(redisTemplate);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(IPage.class)
    public static class IPageNamedRowsLoading {

        @Bean
        @ConditionalOnMissingBean(IPageNamedRowsConvert.class)
        public IPageNamedRowsConvert iPageNamedRowsConvert() {
            return new IPageNamedRowsConvert();
        }
    }

}
