package in.hocg.boot.web.autoconfiguration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import in.hocg.boot.web.autoconfiguration.advice.ErrorPagesConfiguration;
import in.hocg.boot.web.autoconfiguration.core.WarmUpLazyBeanRunner;
import in.hocg.boot.web.autoconfiguration.jackson.SerializerConfiguration;
import in.hocg.boot.web.autoconfiguration.properties.BootProperties;
import in.hocg.boot.web.autoconfiguration.servlet.ServletConfiguration;
import in.hocg.boot.web.autoconfiguration.webflux.WebFluxConfiguration;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@Import({ServletConfiguration.class, WebFluxConfiguration.class,
    SerializerConfiguration.class,
    ErrorPagesConfiguration.class
})
@EnableConfigurationProperties(BootProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebAutoConfiguration {
    private final BootProperties properties;

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .addProperty("hibernate.validator.fail_fast", "true")
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public WarmUpLazyBeanRunner warmUpLazyBeanRunner() {
//        return new WarmUpLazyBeanRunner();
//    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HttpTraceRepository.class)
    public static class HttpTraceConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public HttpTraceRepository httpTraceRepository() {
            return new InMemoryHttpTraceRepository();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(properties.getWorkerId(), properties.getDatacenterId());
    }
}
