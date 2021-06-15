package in.hocg.boot.youtube.autoconfiguration;

import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBootService;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBootServiceImpl;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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
    private final YoutubeBootService bootService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, YoutubeProperties.ClientConfig> configMap = LangUtils.toMap(properties.getClients(), YoutubeProperties.ClientConfig::getClientId);
        bootService.setMultiConfigStorages(configMap);
    }

    @Bean
    @ConditionalOnMissingBean(YoutubeBootService.class)
    public YoutubeBootService mpService() {
        return new YoutubeBootServiceImpl();
    }
}
