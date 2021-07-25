package in.hocg.boot.web.autoconfiguration.core;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by hocgin on 2021/7/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class WarmUpLazyBeanRunner implements ApplicationRunner {

    @Async
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ApplicationContext context = SpringContext.getApplicationContext();
        log.debug("Warm Up Bean Task Start [{}]", WarmUpLazyBeanRunner.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            try {
                context.getBeansOfType(context.getType(beanDefinitionName), false, true);
            } catch (Exception e) {
                log.warn("Warm Up Bean=[{}] Error: {}", beanDefinitionName, e.getMessage());
            }
        }
        log.debug("Warm Up Bean Task End [{}]", WarmUpLazyBeanRunner.class);
    }
}
