package in.hocg.boot.elasticsearch.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(ElasticsearchProperties.PREFIX)
public class ElasticsearchProperties {
    public static final String PREFIX = "boot.elasticsearch";

}
