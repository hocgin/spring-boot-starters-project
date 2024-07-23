package in.hocg.boot.geoip.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(GeoIpProperties.PREFIX)
public class GeoIpProperties {
    public static final String PREFIX = "boot.geoip";

    private Boolean enabled;

    private String countryPath;
    private String cityPath;
}
