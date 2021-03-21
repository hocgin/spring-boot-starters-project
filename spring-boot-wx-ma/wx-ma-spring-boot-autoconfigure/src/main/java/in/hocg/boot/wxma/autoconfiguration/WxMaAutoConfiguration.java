package in.hocg.boot.wxma.autoconfiguration;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
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
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = WxMaProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WxMaProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WxMaAutoConfiguration implements InitializingBean {
    private final WxMaProperties properties;
    private final WxMaService maService;

    @Bean
    @ConditionalOnMissingBean(WxMaService.class)
    public WxMaService maService() {
        return new WxMaServiceImpl();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, WxMaConfig> configs = LangUtils.toMap(properties.getConfigs(), WxMaProperties.Config::getAppid, config -> {
            WxMaDefaultConfigImpl result = new WxMaDefaultConfigImpl();
            result.setAppid(config.getAppid());
            result.setSecret(config.getSecret());
            result.setToken(config.getToken());
            result.setAesKey(config.getAesKey());
            return result;
        });
        maService.setMultiConfigs(configs);
    }
}
