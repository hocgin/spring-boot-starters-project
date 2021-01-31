package in.hocg.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * @author hocgin
 * @date 2019/7/19
 */
@Slf4j
public abstract class SpringContext
    implements ApplicationContextAware, EmbeddedValueResolverAware {

    private static ApplicationContext APPLICATION_CONTEXT;
    private static StringValueResolver STRING_VALUE_RESOLVER;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        SpringContext.STRING_VALUE_RESOLVER = resolver;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    public static StringValueResolver getStringValueResolver() {
        return STRING_VALUE_RESOLVER;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return ((T) APPLICATION_CONTEXT.getBean(name));
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBean(requiredType);
    }

}
