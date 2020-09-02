package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by hocgin on 2020/9/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@Import(SsoClientProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletSsoClientConfiguration {

}
