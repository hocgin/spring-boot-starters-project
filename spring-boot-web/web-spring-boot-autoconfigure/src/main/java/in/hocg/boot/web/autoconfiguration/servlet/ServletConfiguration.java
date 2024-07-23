package in.hocg.boot.web.autoconfiguration.servlet;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.web.autoconfiguration.filter.ContextFilter;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

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
    @Lazy(false)
    public SpringContext springContext() {
        return new SpringServletContext();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean
    public ContextFilter contextFilter() {
        return new ContextFilter();
    }
}
