package in.hocg.boot.mybatis.plus.extensions.config.autoconfiguration;

import in.hocg.boot.mybatis.plus.extensions.config.ConfigMpe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ComponentScan(ConfigMpe.PACKAGE)
public class ConfigMpeAutoConfiguration {

}
