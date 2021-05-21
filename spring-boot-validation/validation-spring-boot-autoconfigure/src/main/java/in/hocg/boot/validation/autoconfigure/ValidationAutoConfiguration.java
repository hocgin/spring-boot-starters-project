package in.hocg.boot.validation.autoconfigure;

import in.hocg.boot.validation.autoconfigure.properties.ValidationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = ValidationProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ValidationProperties.class)
@RequiredArgsConstructor
public class ValidationAutoConfiguration {
    private final ValidationProperties properties;

}
