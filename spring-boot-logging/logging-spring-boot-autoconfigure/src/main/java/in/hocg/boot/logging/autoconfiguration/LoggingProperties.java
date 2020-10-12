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
     * accessKeyId
     */
    private String accessKeyId;
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
