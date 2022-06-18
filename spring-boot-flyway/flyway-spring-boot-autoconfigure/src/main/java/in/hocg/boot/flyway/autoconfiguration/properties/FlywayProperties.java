package in.hocg.boot.flyway.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(FlywayProperties.PREFIX)
public class FlywayProperties {
    public static final String PREFIX = "boot.flyway";

    /**
     * repair-on-migrate default: false
     */
    private Boolean repairOnMigrate = false;
}
