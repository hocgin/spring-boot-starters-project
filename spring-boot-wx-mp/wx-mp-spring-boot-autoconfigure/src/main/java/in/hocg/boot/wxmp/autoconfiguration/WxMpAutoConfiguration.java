package in.hocg.boot.wxmp.autoconfiguration;

import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.wxmp.autoconfiguration.properties.WxMpProperties;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = WxMpProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WxMpProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WxMpAutoConfiguration implements InitializingBean {
    private final WxMpProperties properties;
    private final WxMpService mpServices;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, WxMpConfigStorage> configs = LangUtils.toMap(properties.getConfigs(), WxMpProperties.Config::getAppid, config -> {
            WxMpDefaultConfigImpl result = new WxMpDefaultConfigImpl();
            result.setAppId(config.getAppid());
            result.setSecret(config.getSecret());
            result.setToken(config.getToken());
            result.setAesKey(config.getAesKey());
            return result;
        });
        mpServices.setMultiConfigStorages(configs);
    }

    @Bean
    @ConditionalOnMissingBean(WxMpService.class)
    public WxMpService wxMpBervice() {
        return new WxMpServiceImpl();
    }
}
