package in.hocg.boot.cache.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/5/29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = MyBatisPlusProperties.PREFIX)
public class MyBatisPlusProperties {
    public static final String PREFIX = "mybatis-plus";

}
