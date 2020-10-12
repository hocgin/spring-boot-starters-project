package in.hocg.boot.sms.autoconfigure.properties;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@ToString
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {
    public static final String PREFIX = "boot.sms";
    /**
     * 开启状态
     */
    private Boolean enabled;
    /**
     * Region Id
     */
    private String regionId;
    /**
     * Access Key
     */
    private String accessKey;
    /**
     * Secret Key
     */
    private String secretKey;
    /**
     * SMS Type
     **/
    private SmsType type = SmsType.AliYun;

    public enum SmsType {
        AliYun
    }

}
