package in.hocg.boot.web.autoconfiguration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

/**
 * Created by hocgin on 2021/5/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class AllowBeanDefinitionOverridingApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (applicationContext instanceof AbstractRefreshableApplicationContext) {
            ((AbstractRefreshableApplicationContext) applicationContext).setAllowBeanDefinitionOverriding(true);
        }
    }
}
