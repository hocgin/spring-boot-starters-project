package in.hocg.boot.wxcp.autoconfiguration;

import in.hocg.boot.wxcp.autoconfiguration.properties.WxCpProperties;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = WxCpProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WxCpProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WxCpAutoConfiguration implements InitializingBean {
    private final WxCpProperties properties;
    private final WxCpService cpService;

    @Override
    public void afterPropertiesSet() throws Exception {
        WxCpDefaultConfigImpl configStorage = new WxCpDefaultConfigImpl();
        configStorage.setCorpId(properties.getCorpId());
        configStorage.setCorpSecret(properties.getSecret());
        configStorage.setAgentId(properties.getAgentId());
        configStorage.setToken(properties.getToken());
        configStorage.setAesKey(properties.getAesKey());
        cpService.setWxCpConfigStorage(configStorage);
    }

    @Bean
    @ConditionalOnMissingBean(WxCpService.class)
    public WxCpService mpService() {
        return new WxCpServiceImpl();
    }
}
