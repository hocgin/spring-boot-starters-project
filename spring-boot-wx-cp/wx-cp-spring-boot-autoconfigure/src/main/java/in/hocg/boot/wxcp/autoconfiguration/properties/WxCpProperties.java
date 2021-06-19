package in.hocg.boot.wxcp.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(WxCpProperties.PREFIX)
public class WxCpProperties {
    public static final String PREFIX = "boot.wx.cp";

    /**
     * 企业微信 corpId
     */
    private String corpId;
    /**
     * 企业微信 secret
     */
    private String secret;
    /**
     * 微信企业号应用ID
     */
    private String agentId;
    /**
     * 企业微信 token
     */
    private String token;
    /**
     * 企业微信 aes key
     */
    private String aesKey;
}
