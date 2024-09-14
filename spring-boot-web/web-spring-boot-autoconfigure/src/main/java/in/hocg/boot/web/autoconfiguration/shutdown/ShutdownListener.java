package in.hocg.boot.web.autoconfiguration.shutdown;

import cn.hutool.core.util.ReflectUtil;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.web.autoconfiguration.event.PreExitCodeEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;
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
        shutdownNacos();
        shutdownXXLJob();
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

    public Class getNacosClass() {
        try {
            return Class.forName("com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public Class getXXLJobClass() {
        try {
            return Class.forName("com.xxl.job.core.executor.XxlJobExecutor");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
