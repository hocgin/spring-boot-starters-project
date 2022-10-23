package in.hocg.boot.tpl.autoconfiguration;

import in.hocg.boot.tpl.autoconfiguration.properties.TplProperties;
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
//@AutoConfigureAfter(DataSourceAutoConfiguration.class)
//@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = TplProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(TplProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TplAutoConfiguration {

}
