package in.hocg.boot.oss.autoconfigure.properties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {
    public static final String PREFIX = "boot.oss";
    /**
     * Access Key
     */
    private String accessKey;
    /**
     * Secret Key
     */
    private String secretKey;
    /**
     * Space
     */
    private String space;
    /**
     * Domain
     */
    private String domain;
    /**
     * OSS Type
     */
    private OssType type = OssType.AliYun;

    public enum OssType {
        AliYun,
        QiNiu,
        Local
    }
}
