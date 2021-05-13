package in.hocg.boot.monitor.autoconfiguration;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
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
@ConditionalOnProperty(prefix = MonitorProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MonitorProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MonitorAutoConfiguration {
    @Value("${spring.application.name:unknown}")
    private String applicationName;
    @Value("${spring.profiles.active:unknown}")
    private String profileActive;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName + "-" + profileActive);
    }
}
