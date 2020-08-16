package in.hocg.boot.web.autoconfiguration;

import in.hocg.boot.web.SpringContext;
import in.hocg.boot.web.advice.ErrorPagesConfiguration;
import in.hocg.boot.web.servlet.ServletConfiguration;
import in.hocg.boot.web.webflux.WebFluxConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@Import({ServletConfiguration.class, WebFluxConfiguration.class, ErrorPagesConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebAutoConfiguration {

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }
}
