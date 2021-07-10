package in.hocg.boot.web.autoconfiguration.core;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
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
        log.debug("预热 Bean 开始执行 [{}]", WarmUpLazyBeanRunner.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            try {
                context.getBean(beanDefinitionName);
            } catch (BeansException e) {
                log.warn("预热 Bean=[{}] 发生错误: {}", beanDefinitionName, e);
            } finally {
                log.debug("预热 Bean=[{}]", beanDefinitionName);
            }
        }
        log.debug("预热 Bean 结束执行 [{}]", WarmUpLazyBeanRunner.class);
    }
}
