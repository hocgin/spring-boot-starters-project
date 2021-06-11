package in.hocg.boot.changelog.autoconfiguration;

import in.hocg.boot.changelog.autoconfiguration.core.ChangeLogService;
import in.hocg.boot.changelog.autoconfiguration.jdbc.mysql.ChangeLogServiceImpl;
import in.hocg.boot.changelog.autoconfiguration.properties.ChangeLogProperties;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@ConditionalOnClass({Aspect.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = ChangeLogProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ChangeLogProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ChangeLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChangeLogService changeLogService(DataSource dataSource) {
        return new ChangeLogServiceImpl(dataSource);
    }
}
