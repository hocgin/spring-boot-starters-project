package in.hocg.boot.web.autoconfiguration.webflux;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import(WebFluxExceptionAdvice.class)
public class WebFluxConfiguration {
    @Bean
    public SpringContext springContext() {
        return new SpringWebFluxContext();
    }
}
