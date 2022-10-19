package in.hocg.boot.web.autoconfiguration.core;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;

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
        log.debug("Warm up bean task start [{}]", WarmUpLazyBeanRunner.class);
        String[] reloadBeans = context.getBeanDefinitionNames();
        if (context instanceof AnnotationConfigServletWebServerApplicationContext) {
            AnnotationConfigServletWebServerApplicationContext anContext = (AnnotationConfigServletWebServerApplicationContext) context;
            reloadBeans = Arrays.stream(reloadBeans).filter(beanName -> {
                BeanDefinition beanDefinition = anContext.getBeanDefinition(beanName);
                return beanDefinition.isSingleton() && beanDefinition.isLazyInit();
            }).toArray(String[]::new);
            log.info("Warm up bean use filter context=[{}]", context.getClass());
        } else {
            log.info("Warm up bean not filter context=[{}]", context.getClass());
        }

        for (String beanName : reloadBeans) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Reload lazy bean beanName=[{}]", beanName);
                }
                context.getBean(beanName);
            } catch (Exception e) {
                log.warn("Warm up bean=[{}] error", beanName);
            }
        }
        log.debug("Warm up bean task end [{}]", WarmUpLazyBeanRunner.class);
    }
}
