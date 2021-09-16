package in.hocg.boot.arthas.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(ArthasProperties.PREFIX)
public class ArthasProperties extends com.alibaba.arthas.spring.ArthasProperties {
    public static final String PREFIX = "boot.arthas";

}
