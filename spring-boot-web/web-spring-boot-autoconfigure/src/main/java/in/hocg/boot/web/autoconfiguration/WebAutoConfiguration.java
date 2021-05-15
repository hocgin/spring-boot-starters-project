package in.hocg.boot.web.autoconfiguration;

import in.hocg.boot.web.advice.ErrorPagesConfiguration;
import in.hocg.boot.web.jackson.SerializerConfiguration;
import in.hocg.boot.web.servlet.ServletConfiguration;
import in.hocg.boot.web.webflux.WebFluxConfiguration;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
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
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebAutoConfiguration {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .addProperty("hibernate.validator.fail_fast", "true")
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

//    @Bean
//    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
//        return beanFactory -> ((DefaultListableBeanFactory) beanFactory).setAllowBeanDefinitionOverriding(true);
//    }

}
