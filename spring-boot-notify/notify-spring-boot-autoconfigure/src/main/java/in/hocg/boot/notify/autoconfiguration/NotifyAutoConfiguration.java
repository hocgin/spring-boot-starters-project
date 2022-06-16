package in.hocg.boot.notify.autoconfiguration;

import in.hocg.boot.notify.autoconfiguration.properties.NotifyProperties;
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
@ConditionalOnProperty(prefix = NotifyProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NotifyProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NotifyAutoConfiguration {

}
