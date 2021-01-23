package in.hocg.boot.xxljob.autoconfigure;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {
    public static final String PREFIX = "boot.xxljob";
    /**
     * adminAddresses
     */
    private String adminAddresses;
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * appname
     */
    private String appname;
    /**
     * address
     */
    private String address;
    /**
     * ip
     */
    private String ip;
    /**
     * port
     */
    private Integer port;
    /**
     * logPath
     */
    private String logPath;
    /**
     * logRetentionDays
     */
    private Integer logRetentionDays = -1;
}
