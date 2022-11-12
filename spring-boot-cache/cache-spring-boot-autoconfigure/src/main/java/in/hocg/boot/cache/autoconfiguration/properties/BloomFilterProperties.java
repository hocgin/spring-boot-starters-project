package in.hocg.boot.cache.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(BloomFilterProperties.PREFIX)
public class BloomFilterProperties {
    public static final String PREFIX = "boot.bloom";
    /**
     * 布隆过滤器名称
     */
    private String name;

    /**
     * 预计数据量(默认10w)
     */
    private Long expectedInsertions = 10 * 10000L;

    /**
     * 错误率(默认: 0.25)
     */
    private Double fpp = 0.25;
}
