package in.hocg.boot.test.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(TestProperties.PREFIX)
public class TestProperties {
    public static final String PREFIX = "boot.test";

}
