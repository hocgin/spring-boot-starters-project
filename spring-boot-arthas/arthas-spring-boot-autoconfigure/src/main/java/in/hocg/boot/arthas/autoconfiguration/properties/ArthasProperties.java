package in.hocg.boot.arthas.autoconfiguration.properties;

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
@ConfigurationProperties(ArthasProperties.PREFIX)
public class ArthasProperties extends com.alibaba.arthas.spring.ArthasProperties {
    public static final String PREFIX = "boot.arthas";

}
