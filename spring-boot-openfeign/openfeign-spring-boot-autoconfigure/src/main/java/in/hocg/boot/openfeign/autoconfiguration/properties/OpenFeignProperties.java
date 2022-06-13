package in.hocg.boot.openfeign.autoconfiguration.properties;

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
@ConfigurationProperties(OpenFeignProperties.PREFIX)
public class OpenFeignProperties {
    public static final String PREFIX = "boot.openfeign";

    /**
     * X-User-Agent 标记。默认: feign
     */
    private String userAgent = "openfeign";
}
