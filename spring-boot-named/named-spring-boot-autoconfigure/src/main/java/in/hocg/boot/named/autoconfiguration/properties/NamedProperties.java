package in.hocg.boot.named.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Created by hocgin on 2021/7/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@ConfigurationProperties(prefix = NamedProperties.PREFIX)
public class NamedProperties {
    public static final String PREFIX = "boot.named";

    /**
     * 缓存配置
     */
    private CacheConfig cache = CacheConfig.DEFAULT;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class CacheConfig {
        public static final CacheConfig DEFAULT = new CacheConfig().setPrefix("v").setExpired(Duration.ofDays(1));

        /**
         * 缓存前缀
         */
        private String prefix;
        /**
         * 过期时间
         */
        private Duration expired;

    }
}
