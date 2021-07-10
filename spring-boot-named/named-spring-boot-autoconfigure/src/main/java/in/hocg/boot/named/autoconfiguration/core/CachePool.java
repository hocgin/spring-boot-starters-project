package in.hocg.boot.named.autoconfiguration.core;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Maps;
import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.NamedService;
import in.hocg.boot.named.autoconfiguration.utils.NamedUtils;
import in.hocg.boot.named.ifc.NamedHandler;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Created by hocgin on 2021/7/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class CachePool {
    public static Map<Class<?>, Object> NAMED_SERVICE_CLASS_MAPS = new WeakHashMap<>();

    /**
     * 预热缓存
     *
     * @param context
     */
    public static void load(ApplicationContext context) {
        // 1.1 预热 NamedBean
        Map<String, NamedService> namedServiceMaps = context.getBeansOfType(NamedService.class);
        Map<Class<?>, Object> namedMaps = Maps.newHashMap();
        for (NamedService namedService : namedServiceMaps.values()) {
            for (Class<?> aClass : AopProxyUtils.proxiedUserInterfaces(namedService)) {
                namedMaps.put(aClass, namedService);
            }
        }
        namedMaps.computeIfAbsent(NamedService.class, aClass -> namedServiceMaps.values().stream().findFirst().orElse(null));
        NAMED_SERVICE_CLASS_MAPS = namedMaps;

        // 1.2 预热 NamedClass
        namedMaps.keySet().forEach(serviceClass -> Arrays.stream(ReflectUtil.getMethods(serviceClass))
            .map(ClassUtils::getInterfaceMethodIfPossible)
            .filter(method -> Objects.nonNull(method.getAnnotation(NamedHandler.class)))
            .forEach(method -> {
                final NamedHandler annotation = method.getAnnotation(NamedHandler.class);
                NamedUtils.getNamedHandleMethod(serviceClass, annotation.value());
            })
        );

        // 2. 扫描字段
        ClassUtil.scanPackage().stream().filter(aClass -> aClass.isAnnotationPresent(InjectNamed.class)).forEach(NamedUtils::getAllField);
    }
}
