package in.hocg.boot.logging.autoconfiguration;

import in.hocg.boot.logging.autoconfiguration.core.DefaultLoggerListener;
import in.hocg.boot.logging.autoconfiguration.core.LoggerAspect;
import in.hocg.boot.logging.autoconfiguration.core.LoggerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
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
@EnableConfigurationProperties({LoggingProperties.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LoggerListener loggerListener() {
        return new DefaultLoggerListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggerAspect loggerAspect(ApplicationEventPublisher publisher) {
        return new LoggerAspect(publisher);
    }
}
