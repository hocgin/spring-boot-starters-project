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
@ConfigurationProperties(OpenfeignProperties.PREFIX)
public class OpenfeignProperties {
    public static final String PREFIX = "boot.openfeign";
}
