package in.hocg.boot.openfeign.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(OpenFeignProperties.PREFIX)
public class OpenFeignProperties {
    public static final String PREFIX = "boot.openfeign";
}
