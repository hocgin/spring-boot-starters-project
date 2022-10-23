package in.hocg.boot.netty.server.autoconfiguration.bean;

import in.hocg.netty.core.annotation.Command;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.netty.core.invoker.BeInvokerMethod;
import in.hocg.netty.core.invoker.BeInvokerManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author hocgin
 */
@Slf4j
@Getter
@Setter
public class CommandScanner implements ApplicationRunner {

    public static void scan() {
        ApplicationContext context = SpringContext.getApplicationContext();
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            CommandScanner.registerAllCommand(beanName);
        }
    }

    private static Object getBeanObject(String beanName) {
        try {
            return SpringContext.getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }

    private static void registerAllCommand(String beanName) {
        Object beanObject = getBeanObject(beanName);
        if (Objects.isNull(beanObject)) {
            return;
        }

        Class<?> clazz = beanObject.getClass();
        for (Method method : clazz.getMethods()) {
            Command command = method.getAnnotation(Command.class);
            if (Objects.isNull(command)) {
                continue;
            }
            BeInvokerManager.registerCommand(command, BeInvokerMethod.of(beanObject, method));
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CommandScanner.scan();
    }
}
