package in.hocg.boot.dataaudit.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(DataAuditProperties.PREFIX)
public class DataAuditProperties {
    public static final String PREFIX = "boot.data-audit";

}