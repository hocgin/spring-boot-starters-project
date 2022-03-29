package in.hocg.boot.javacv.autoconfiguration;

import in.hocg.boot.javacv.autoconfiguration.properties.JavaCvProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 * http://ffmpeg.org/download.html
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = JavaCvProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(JavaCvProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class JavaCvAutoConfiguration {

}
