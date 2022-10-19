package in.hocg.boot.netty.server.autoconfiguration.bean;

import cn.hutool.core.util.ClassUtil;
import in.hocg.boot.netty.server.autoconfiguration.annotation.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class CommandScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    private String basePackage;
    private Class<? extends Annotation> annotationClass;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS)
        Set<Class<?>> classes = ClassUtil.scanPackage(this.basePackage, aClass ->
            Arrays.stream(ClassUtil.getPublicMethods(aClass)).anyMatch(method -> method.isAnnotationPresent(annotationClass)));
        registerAllCommand(classes);
    }

    private void registerAllCommand(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                Command command = method.getAnnotation(Command.class);
                if (Objects.isNull(command)) {
                    continue;
                }
                String key = InvokerManager.genKey(command.module(), command.value());
                registerContainer(key, Invoker.valueOf(method, clazz));
            }
        }
    }

    private void registerContainer(String key, Invoker invoker) {
        if (InvokerManager.METHODS.containsKey(key)) {
            log.error("标识({})已经存在", key);
        } else {
            log.debug("类名: {}, 函数: {} => {}", invoker.getTargetClass(), invoker.getMethod().getName(), key);
            InvokerManager.METHODS.put(key, invoker);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // ..
    }
}
