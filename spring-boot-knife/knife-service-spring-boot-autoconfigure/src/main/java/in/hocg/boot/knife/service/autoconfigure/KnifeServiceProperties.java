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

    private Boolean enabled = true;
    private String version = "1.0";
    private String description = "这个人很懒没有填写";
    private Contact contact;
    private String license;
    private String licenseUrl;
}
