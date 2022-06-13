package in.hocg.boot.logging.autoconfiguration.properties;

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
@ConfigurationProperties(LoggingProperties.PREFIX)
public class LoggingProperties {
    public static final String PREFIX = "boot.logging";

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * logstore
     */
    private String logStore;
    /**
     * endpoint
     */
    private String endpoint;
    /**
     * secretKey
     */
    private String secretKey;
    /**
     * access-key
     */
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
