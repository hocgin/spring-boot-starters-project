package in.hocg.boot.named.autoconfiguration.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import in.hocg.boot.named.ifc.NamedHandler;
import in.hocg.boot.utils.LangUtils;
import lombok.experimental.UtilityClass;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2021/6/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class NamedUtils {
    private static final Map<String, Method> SERVICE_NAMED_METHOD_MAPS = Maps.newConcurrentMap();

    public Map<String, Field> getAllField(Class<?> clazz) {
        return ReflectUtil.getFieldMap(clazz);
    }

    public Object getFieldValue(Object target, Field field) {
        return ReflectUtil.getFieldValue(target, field);
    }

    public void setFieldValue(Object target, Field field, Object value) {
        ReflectUtil.setFieldValue(target, field, value);
    }

    public static Method getNamedHandleMethod(final Class<?> serviceClass, String namedType) {
        return LangUtils.computeIfAbsent(SERVICE_NAMED_METHOD_MAPS, StrUtil.format("{}.{}", serviceClass.getName(), namedType),
            (s) -> NamedUtils.getNamedHandlerMethod(serviceClass, namedType).orElse(null));
    }


    /**
     * 获取处理函数
     *
     * @param serviceClass _
     * @param namedType    _
     * @return _
     */
    public static Optional<Method> getNamedHandlerMethod(Class<?> serviceClass, String namedType) {
        return Arrays.stream(ReflectUtil.getMethods(serviceClass)).parallel()
            .map(ClassUtils::getInterfaceMethodIfPossible)
            .filter(method -> {
                final NamedHandler annotation = method.getAnnotation(NamedHandler.class);
                if (Objects.isNull(annotation)) {
                    return false;
                }
                return annotation.value().equals(namedType);
            }).findFirst();
    }
}
