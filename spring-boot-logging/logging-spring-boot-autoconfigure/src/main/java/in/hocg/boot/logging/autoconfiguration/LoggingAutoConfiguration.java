package in.hocg.boot.logging.autoconfiguration;

import lombok.RequiredArgsConstructor;
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
@EnableConfigurationProperties({LoggingProperties.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LoggingAutoConfiguration {

}
