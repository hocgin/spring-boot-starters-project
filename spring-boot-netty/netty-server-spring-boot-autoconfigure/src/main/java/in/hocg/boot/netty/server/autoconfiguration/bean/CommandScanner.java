package in.hocg.boot.netty.server.autoconfiguration.bean;

import in.hocg.boot.netty.server.autoconfiguration.annotation.Command;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.netty.core.invoker.Invoker;
import in.hocg.netty.core.invoker.InvokerManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Objects;

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
            registerContainer(InvokerManager.ofKey(command.module(), command.value()), Invoker.of(beanObject, method));
        }
    }

    private static void registerContainer(String key, Invoker invoker) {
        if (InvokerManager.METHODS.containsKey(key)) {
            log.error("标识({})已经存在", key);
        } else {
            log.debug("类名: {}, 函数: {} => {}", invoker.getTarget().getClass(), invoker.getMethod().getName(), key);
            InvokerManager.METHODS.put(key, invoker);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CommandScanner.scan();
    }
}
