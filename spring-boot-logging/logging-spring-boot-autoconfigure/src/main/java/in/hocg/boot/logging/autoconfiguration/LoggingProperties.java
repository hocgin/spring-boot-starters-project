package in.hocg.boot.logging.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(LoggingProperties.PREFIX)
public class LoggingProperties {
    public static final String PREFIX = "boot.logging";

    private String projectName;
    private String logStore;
    private String endpoint;
    private String accessKeyId;
    private String accessKey;
    /**
     * Topic
     */
    private String topic;
    /**
     * 来源
     */
    private String source;

}
