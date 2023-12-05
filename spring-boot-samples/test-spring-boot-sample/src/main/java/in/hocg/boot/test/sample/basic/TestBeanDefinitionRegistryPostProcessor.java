package in.hocg.boot.test.sample.basic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2023/12/05
 * email: hocgin@gmail.com
 * 动态注册 BeanDefinition
 *
 * @author hocgin
 */
@Component
public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        TestClassPathBeanDefinitionScanner scanner = new TestClassPathBeanDefinitionScanner(registry);
        scanner.registerFilters();
        scanner.scan(this.getClass().getPackageName());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
