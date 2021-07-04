package in.hocg.boot.sso.client.autoconfigure;

import in.hocg.boot.sso.client.autoconfigure.core.servlet.ServletSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.WebFluxSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SsoClientProperties.class)
@Import({ServletSsoClientConfiguration.class, WebFluxSsoClientConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SsoClientAutoConfiguration {
    private final SsoClientProperties properties;

}
