package in.hocg.boot.cache.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(BloomFilterProperties.PREFIX)
public class BloomFilterProperties {
    public static final String PREFIX = "boot.bloom";
    /**
     * Bloom name
     */
    private String name;

    /**
     * 预计数据量
     */
    private Long expectedInsertions;

    /**
     * 错误率
     */
    private Double fpp;
}
