package in.hocg.boot.mybatis.plus.extensions.userconfig.autoconfiguration;

import in.hocg.boot.mybatis.plus.extensions.userconfig.UserConfigMpe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = UserConfigMybatisPlusExtProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(UserConfigMybatisPlusExtProperties.class)
@ComponentScan(UserConfigMpe.PACKAGE)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserConfigMybatisPlusExtAutoConfiguration {
}
