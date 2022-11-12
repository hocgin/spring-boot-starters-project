package in.hocg.boot.mybatis.plus.extensions.changelog.autoconfiguration;

import in.hocg.boot.mybatis.plus.extensions.changelog.ChangeLogMpe;
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
@ComponentScan(ChangeLogMpe.PACKAGE)
public class ChangeLogMybatisPlusExtAutoConfiguration {
}
