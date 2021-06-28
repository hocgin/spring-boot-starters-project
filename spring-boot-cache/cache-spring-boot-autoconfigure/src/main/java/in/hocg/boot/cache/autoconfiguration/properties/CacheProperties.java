package in.hocg.boot.cache.autoconfiguration.properties;

import in.hocg.boot.utils.StringPoolUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@RefreshScope
@ConfigurationProperties(CacheProperties.PREFIX)
public class CacheProperties {
    public static final String COLON = StringPoolUtils.COLON;
    public static final String PREFIX = "boot.cache";

    /**
     * 缓存配置
     */
    private List<CacheName> names = Collections.emptyList();

    @Data
    public static class CacheName {
        private String name;
        private Duration ttl;
    }
}
