package in.hocg.boot.youtube.autoconfiguration;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBervice;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBerviceImpl;
import in.hocg.boot.youtube.autoconfiguration.core.datastore.RedisDataStoreFactory;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = YoutubeProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(YoutubeProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class YoutubeAutoConfiguration implements InitializingBean {
    private final YoutubeProperties properties;
    private final YoutubeBervice bootService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, YoutubeProperties.ClientConfig> configMap = LangUtils.toMap(properties.getClients(), YoutubeProperties.ClientConfig::getClientId);
        bootService.setMultiConfigStorages(configMap);
    }

    @Bean
    @ConditionalOnMissingBean(YoutubeBervice.class)
    public YoutubeBervice youtubeBervice() {
        return new YoutubeBerviceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataStoreFactory memoryDataStoreFactory() {
        return MemoryDataStoreFactory.getDefaultInstance();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(StringRedisTemplate.class)
    public static class RedisDataStoreConfiguration {
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @ConditionalOnMissingBean
        public DataStoreFactory redisDataStoreFactory(StringRedisTemplate redisTemplate) {
            return new RedisDataStoreFactory(redisTemplate);
        }
    }
}
