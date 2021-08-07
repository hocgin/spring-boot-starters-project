package in.hocg.boot.http.log.autoconfiguration;

import in.hocg.boot.http.log.autoconfiguration.core.HttpLogBervice;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLogBerviceImpl;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLogRepository;
import in.hocg.boot.http.log.autoconfiguration.jdbc.mysql.HttpLogRepositoryImpl;
import in.hocg.boot.http.log.autoconfiguration.properties.HttpLogProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@EnableAsync
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = HttpLogProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(HttpLogProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class HttpLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpLogRepository httpLogRepository(DataSource dataSource) {
        return new HttpLogRepositoryImpl(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpLogBervice httpLogBervice() {
        return new HttpLogBerviceImpl();
    }
}
