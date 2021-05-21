package in.hocg.boot.excel.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(ExcelProperties.PREFIX)
public class ExcelProperties {
    public static final String PREFIX = "boot.excel";

}
