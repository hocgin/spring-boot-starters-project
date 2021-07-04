package in.hocg.sso.server2.sample.config.security.autoconfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2020/8/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableConfigurationProperties(SsoProperties.class)
public class SsoAutoConfiguration {

}
