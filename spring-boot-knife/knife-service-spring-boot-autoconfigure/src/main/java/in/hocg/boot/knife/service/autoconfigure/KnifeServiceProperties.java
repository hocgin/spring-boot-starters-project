package in.hocg.boot.knife.service.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = KnifeServiceProperties.PREFIX)
public class KnifeServiceProperties {
    public static final String PREFIX = "boot.knife.service";
    /**
     * 开启状态
     */
    private Boolean enabled = true;
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 描述
     */
    private String description = "这个人很懒没有填写";
    /**
     * 联系方式
     */
    private Contact contact;
    /**
     * 协议
     */
    private String license;
    /**
     * 协议URL
     */
    private String licenseUrl;
}
