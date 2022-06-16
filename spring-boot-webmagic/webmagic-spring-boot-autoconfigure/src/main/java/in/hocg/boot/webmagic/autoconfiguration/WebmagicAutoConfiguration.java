package in.hocg.boot.webmagic.autoconfiguration;

import in.hocg.boot.webmagic.autoconfiguration.properties.WebmagicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ComponentScan(basePackageClasses = WebmagicAutoConfiguration.class)
@ConditionalOnProperty(prefix = WebmagicProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WebmagicProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebmagicAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("WebmagicAutoConfiguration afterPropertiesSet");
    }
}
