package in.hocg.boot.web.autoconfiguration.shutdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.web.autoconfiguration.event.PreExitCodeEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by hocgin on 2024/08/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class ShutdownListener implements ApplicationListener<PreExitCodeEvent> {


    @Override
    public void onApplicationEvent(PreExitCodeEvent preExitCodeEvent) {
        tryCatch(this::shutdownNacos);
        tryCatch(this::shutdownXXLJob);
        tryCatch(this::shutdownKafka);
    }

    @SneakyThrows
    public void shutdownNacos() {
        Class clazz = getNacosClass();
        if (Objects.isNull(clazz)) {
            log.info("未找到 Nacos");
            return;
        }
        log.debug("正在下线 Nacos ..");
        Object bean = SpringContext.getBean(clazz);
        Method destroy = ReflectUtil.getMethodByName(clazz, "stop");
        destroy.invoke(bean);
    }


    @SneakyThrows
    public void shutdownXXLJob() {
        Class clazz = getXXLJobClass();
        if (Objects.isNull(clazz)) {
            log.info("未找到 XXLJob");
            return;
        }
        log.debug("正在下线 XXLJob ..");
        Object bean = SpringContext.getBean(clazz);
        Method destroy = ReflectUtil.getMethodByName(clazz, "destroy");
        destroy.invoke(bean);
    }

    @SneakyThrows
    public void shutdownKafka() {
        Class clazz = getKafkaClass();
        if (Objects.isNull(clazz)) {
            log.info("未找到 Kafka");
            return;
        }
        log.debug("正在下线 Kafka ..");
        Object bean = SpringContext.getBean(clazz);
        Method destroy = ReflectUtil.getMethodByName(clazz, "getListenerContainers");
        Collection<org.springframework.kafka.listener.MessageListenerContainer> containers = (Collection<MessageListenerContainer>) destroy.invoke(bean);
        if (CollUtil.isEmpty(containers)) {
            return;
        }
        for (org.springframework.kafka.listener.MessageListenerContainer container : containers) {
            if (container.isRunning()) {
                container.stop();
            }
        }
    }

    public Class getKafkaClass() {
        try {
            return Class.forName("org.springframework.kafka.config.KafkaListenerEndpointRegistry");
        } catch (Exception e) {
            return null;
        }
    }

    public Class getNacosClass() {
        try {
            return Class.forName("com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration");
        } catch (Exception e) {
            return null;
        }
    }

    public Class getXXLJobClass() {
        try {
            return Class.forName("com.xxl.job.core.executor.XxlJobExecutor");
        } catch (Exception e) {
            return null;
        }
    }

    public static void tryCatch(Runnable runnable) {
        if (Objects.isNull(runnable)) {
            return;
        }
        try {
            runnable.run();
        } catch (Exception e) {
            log.warn("发生可忽略异常", e);
        }
    }
}
