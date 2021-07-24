package in.hocg.boot.web.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2021/6/19
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(BootProperties.PREFIX)
public class BootProperties {
    public static final String PREFIX = "boot.web";
    /**
     * 配置域名
     */
    private String hostname;
    /**
     * 是否调试模式
     */
    private Boolean isDebug = Boolean.FALSE;
}
