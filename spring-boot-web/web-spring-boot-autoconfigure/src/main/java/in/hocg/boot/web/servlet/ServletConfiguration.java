package in.hocg.boot.web.servlet;

import in.hocg.boot.web.SpringContext;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.Servlet;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, Tomcat.class})
@Import(ServletExceptionAdvice.class)
public class ServletConfiguration {
    @Bean
    public SpringContext springContext() {
        return new SpringServletContext();
    }
}
