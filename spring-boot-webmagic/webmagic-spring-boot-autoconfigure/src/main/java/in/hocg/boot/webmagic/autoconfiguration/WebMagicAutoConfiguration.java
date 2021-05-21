package in.hocg.boot.webmagic.autoconfiguration;

import in.hocg.boot.webmagic.autoconfiguration.properties.WebMagicProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = WebMagicProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WebMagicProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebMagicAutoConfiguration {

}
