package in.hocg.boot.named.autoconfiguration.core;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Maps;
import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.NamedService;
import in.hocg.boot.named.autoconfiguration.utils.NamedUtils;
import in.hocg.boot.named.ifc.NamedHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/7/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class CachePool {
    public static Map<Class<?>, Object> NAMED_SERVICE_CLASS_MAPS = new WeakHashMap<>();
    public static Map<Class<?>, Boolean> IS_SUPPORT_NAMED_CLASS_MAPS = new ConcurrentHashMap<>();

    /**
     * 预热缓存
     *
     * @param context
     */
    @SneakyThrows
    public static void load(ApplicationContext context) {
        // 1.1 预热 NamedBean
        Map<String, NamedService> namedServiceMaps = context.getBeansOfType(NamedService.class);
        Map<Class<?>, Object> namedMaps = Maps.newHashMap();
        for (NamedService namedService : namedServiceMaps.values()) {
            namedMaps.put(namedService.getClass(), namedService);
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
        Reflections reflections = new Reflections(".*", new SubTypesScanner(), new TypeAnnotationsScanner());
        reflections.getTypesAnnotatedWith(InjectNamed.class).forEach(NamedUtils::getAllField);
    }

    /**
     * 判断一个类是否需要被 @Named 处理
     *
     * @param clazz
     * @param isSupportFunc
     * @return
     */
    public static Boolean isSupportNamed(Class<?> clazz, Function<Class<?>, Boolean> isSupportFunc) {
        Boolean result = IS_SUPPORT_NAMED_CLASS_MAPS.get(clazz);
        if (Objects.isNull(result)) {
            result = isSupportFunc.apply(clazz);
            IS_SUPPORT_NAMED_CLASS_MAPS.put(clazz, result);
        }
        return result;
    }

}
