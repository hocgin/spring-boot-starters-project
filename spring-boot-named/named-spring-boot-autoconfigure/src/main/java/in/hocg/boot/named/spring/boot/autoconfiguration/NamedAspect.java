package in.hocg.boot.named.spring.boot.autoconfiguration;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import in.hocg.boot.named.spring.boot.autoconfiguration.core.ClassName;
import in.hocg.boot.named.spring.boot.autoconfiguration.core.LangUtils;
import in.hocg.boot.named.spring.boot.autoconfiguration.annotation.InjectNamed;
import in.hocg.boot.named.spring.boot.autoconfiguration.annotation.Named;
import in.hocg.boot.named.spring.boot.autoconfiguration.core.NamedRow;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedArgs;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedHandler;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

/**
 * @author hocgin
 * @date 2017/11/17
 * email: hocgin@gmail.com
 * 自动填充类型对应的名称
 */
@Slf4j
@Aspect
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NamedAspect {
    private final ApplicationContext context;
    private final ThreadLocal<Map<String, Object>> cache = ThreadLocal.withInitial(WeakHashMap::new);

    @Pointcut("@within(org.springframework.stereotype.Service) && execution((*) *(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        handleResult(result);
        return result;
    }

    private void handleResult(Object result) {
        List<NamedRow> namedRows = getNamedRows(result);
        Map<String, List<NamedRow>> namedGroup = namedRows.parallelStream().collect(Collectors.groupingBy(this::getGroupKey));
        namedGroup.values().parallelStream().forEach(this::injectValue);
    }

    private String getGroupKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        String[] args = namedRow.getArgs();
        return namedType + "@" + Arrays.toString(args);
    }

    private List<NamedRow> getNamedRows(Object result) {
        if (Objects.isNull(result) || LangUtils.isBaseType(result.getClass())) {
            return Collections.emptyList();
        }

        if (ClassName.isIPageClass(result)) {
            return getPageNamedRows(result);
        } else if (result instanceof Collection) {
            return getCollectionNamedRows((Collection<?>) result);
        } else if (result instanceof Object[]) {
            return getArrayNamedRows((Object[]) result);
        } else {
            return getObjectNamedRows(result);
        }
    }

    private List<NamedRow> getObjectNamedRows(Object result) {
        final Class<?> aClass = result.getClass();
        if (!aClass.isAnnotationPresent(InjectNamed.class)) {
            return Collections.emptyList();
        }

        List<NamedRow> namedRows = Lists.newArrayList();
        Map<String, Field> fieldMap = LangUtils.toMap(LangUtils.getAllField(aClass), Field::getName);
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            final Object value = LangUtils.getObjectValue(result, field, null);
            namedRows.addAll(getNamedRows(value));
            boolean useNamed = field.isAnnotationPresent(Named.class);
            if (!useNamed || Objects.nonNull(value)) {
                continue;
            }
            getNamedRow(result, fieldMap, field).ifPresent(namedRows::add);
        }

        return namedRows;
    }

    private Optional<NamedRow> getNamedRow(Object target, Map<String, Field> fieldMap, Field targetField) {
        final Named named = targetField.getAnnotation(Named.class);
        final Field idField = fieldMap.get(named.idFor());
        if (Objects.isNull(idField)) {
            return Optional.empty();
        }
        String[] argsValue = named.args();
        if (argsValue.length == 0) {
            argsValue = new String[]{named.idFor()};
        }
        final String namedType = named.type();
        final Object idValue = LangUtils.getObjectValue(target, idField, null);
        NamedRow namedRow = new NamedRow()
            .setTarget(target)
            .setIdValue(idValue)
            .setArgs(argsValue)
            .setTargetField(targetField)
            .setNamedType(namedType);
        return Optional.of(namedRow);
    }

    private List<NamedRow> getArrayNamedRows(Object[] result) {
        return getCollectionNamedRows(Lists.newArrayList(result));
    }

    private List<NamedRow> getCollectionNamedRows(Collection<?> result) {
        return result.parallelStream().flatMap(o -> getNamedRows(o).stream())
            .collect(Collectors.toList());
    }

    private List<NamedRow> getPageNamedRows(Object result) {
        return this.getNamedRows(((IPage<?>) result).getRecords());
    }

    private void injectValue(List<NamedRow> namedRows) {
        injectValueWithCache(namedRows);
        injectValueWithMethod(namedRows);
    }

    private void injectValueWithCache(List<NamedRow> namedRows) {
        namedRows.parallelStream().forEach(namedRow -> {
            Object value = cache.get().get(getCacheKey(namedRow));
            setValue(namedRow, value);
        });
    }

    private void injectValueWithMethod(List<NamedRow> namedRows) {
        List<NamedRow> newNamedRows = namedRows.parallelStream()
            .filter(namedRow -> Objects.isNull(namedRow.getTargetValue()))
            .collect(Collectors.toList());
        if (newNamedRows.isEmpty()) {
            return;
        }
        NamedRow namedRow = newNamedRows.get(0);
        String namedType = namedRow.getNamedType();
        String[] args = namedRow.getArgs();
        Object[] ids = newNamedRows.parallelStream().map(NamedRow::getIdValue).distinct().toArray();
        if (ids.length == 0) {
            return;
        }
        Map<String, Object> values = callNamedHandleMethod(namedType, ids, args);
        log.info("===> {}-{}-{}::{}", namedType, ids, args, values);
        newNamedRows.parallelStream().forEach(row -> {
            Object value = values.get(LangUtils.toString(row.getIdValue()));
            if (Objects.nonNull(value)) {
                setValue(row, value);
            }
        });
    }

    private void setValue(NamedRow namedRow, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        namedRow.setTargetValue(value);
        LangUtils.setFieldValue(namedRow.getTarget(), namedRow.getTargetField(), value);
        cache.get().put(getCacheKey(namedRow), value);
    }

    private String getCacheKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        Object id = namedRow.getIdValue();
        String[] args = namedRow.getArgs();
        return String.format("%s-%s-%s", namedType, id, Arrays.toString(args));
    }

    private Map<String, Object> callNamedHandleMethod(String namedType, Object[] ids, String[] args) {
        final NamedService namedService = context.getBean(NamedService.class);

        for (Method method : namedService.getClass().getMethods()) {
            final NamedHandler annotation = method.getAnnotation(NamedHandler.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            final String value = annotation.value();
            if (value.equals(namedType)) {
                NamedArgs namedArgs = new NamedArgs().setArgs(args).setValues(Lists.newArrayList(ids));
                try {
                    Object invokeResult = method.invoke(namedService, namedArgs);
                    if (Objects.isNull(invokeResult)) {
                        return Collections.emptyMap();
                    } else if (invokeResult instanceof Map) {
                        return (Map<String, Object>) invokeResult;
                    } else {
                        return Collections.emptyMap();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Method Invoke Error: ", e);
                    throw new NamedInvokeMethodException("服务调用失败", e);
                }
            }
        }
        return Collections.emptyMap();
    }

}
