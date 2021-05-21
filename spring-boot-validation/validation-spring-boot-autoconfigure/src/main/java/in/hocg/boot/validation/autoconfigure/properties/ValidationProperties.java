package in.hocg.boot.validation.autoconfigure.properties;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/10/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@ToString
@ConfigurationProperties(prefix = ValidationProperties.PREFIX)
public class ValidationProperties {
    public static final String PREFIX = "boot.validation";
    /**
     * 开启状态
     */
    private Boolean enabled;
}
