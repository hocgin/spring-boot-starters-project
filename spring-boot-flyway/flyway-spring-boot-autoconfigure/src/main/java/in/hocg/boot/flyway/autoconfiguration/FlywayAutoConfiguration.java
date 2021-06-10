package in.hocg.boot.flyway.autoconfiguration;

import in.hocg.boot.flyway.autoconfiguration.properties.FlywayProperties;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(prefix = FlywayProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(FlywayProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class FlywayAutoConfiguration {
    private FlywayProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = FlywayProperties.PREFIX + ".repair-on-migrate", havingValue = "true")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
