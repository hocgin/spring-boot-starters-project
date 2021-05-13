package in.hocg.boot.excel.autoconfiguration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = ExcelProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ExcelProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ExcelAutoConfiguration {

}
