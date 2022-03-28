package in.hocg.boot.javacv.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(JavaCvProperties.PREFIX)
public class JavaCvProperties {
    public static final String PREFIX = "boot.java-cv";

}
