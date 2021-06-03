package in.hocg.boot.named.autoconfiguration.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.boot.named.annotation.NamedService;
import in.hocg.boot.named.annotation.UseNamedService;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.NamedRow;
import in.hocg.boot.named.autoconfiguration.core.convert.NamedRowsConvert;
import in.hocg.boot.named.ifc.NamedArgs;
import in.hocg.boot.named.ifc.NamedHandler;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
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
public class NamedAspect implements InitializingBean {
    private final ApplicationContext context;
    private final List<NamedRowsConvert> converts;
    private NamedCacheService cacheService;
    /**
     * \\@UseNamedService 服务类缓存
     */
    protected final Map<String, Class<?>> serviceClassMaps = Maps.newConcurrentMap();

    /**
     * \\@NamedHandler 处理函数缓存
     */
    protected final Map<String, Method> serviceMethodMaps = Maps.newConcurrentMap();

    @Pointcut("@within(org.springframework.stereotype.Service) && execution((*) *(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        return handleResult(proceed);
    }

    private Object handleResult(Object result) {
        List<NamedRow> namedRows = getNamedRows(result);

        if (CollectionUtils.isEmpty(namedRows)) {
            return result;
        }
        Map<String, List<NamedRow>> namedGroup = namedRows.parallelStream()
            .collect(Collectors.groupingBy(this::getGroupKey));

        namedGroup.values().forEach(this::injectValue);
        return result;
    }

    private List<NamedRow> getNamedRows(Object result) {
        if (Objects.isNull(result) || LangUtils.isBaseType(result.getClass())) {
            return Collections.emptyList();
        } else if (result instanceof Collection) {
            return getCollectionNamedRows((Collection<?>) result);
        } else if (result instanceof Object[]) {
            return getArrayNamedRows((Object[]) result);
        }

        // 扩展点
        Optional<NamedRowsConvert> convertOpt = this.converts.stream()
            .filter(namedRowsConvert -> namedRowsConvert.isMatch(result)).findFirst();
        if (convertOpt.isPresent()) {
            NamedRowsConvert convert = convertOpt.get();
            Object convertResult = convert.convert(result);
            if (Objects.isNull(convertResult) || convertResult.getClass().equals(result.getClass())) {
                log.warn("在 @Named 扩展了非法转换器[{}], 请及时更正", convert.getClass());
                return Collections.emptyList();
            }
            return getNamedRows(convertResult);
        }

        return getObjectNamedRows(result);
    }

    private List<NamedRow> getObjectNamedRows(Object result) {
        final Class<?> aClass = result.getClass();
        if (!aClass.isAnnotationPresent(InjectNamed.class)) {
            return Collections.emptyList();
        }

        // 解析对象的字段
        List<NamedRow> namedRows = Lists.newCopyOnWriteArrayList();
        Map<String, Field> fieldMap = LangUtils.toMap(LangUtils.getAllField(aClass), Field::getName);
        fieldMap.entrySet().parallelStream().forEach(entry -> {
            List<NamedRow> rows = Lists.newArrayList();
            Field field = entry.getValue();
            final Object value = LangUtils.getObjectValue(result, field, null);
            rows.addAll(getNamedRows(value));
            boolean useNamed = field.isAnnotationPresent(Named.class);
            if (useNamed && Objects.isNull(value)) {
                getNamedRow(result, fieldMap, field).ifPresent(rows::add);
            }
            namedRows.addAll(rows);
        });

        return namedRows;
    }

    private Optional<NamedRow> getNamedRow(Object target, Map<String, Field> fieldMap, Field targetField) {
        final Named named = targetField.getAnnotation(Named.class);
        UseNamedService useService = targetField.getAnnotation(UseNamedService.class);
        final Field idField = fieldMap.get(named.idFor());
        if (Objects.isNull(idField)) {
            return Optional.empty();
        }
        String[] argsValue = named.args();
        if (argsValue.length == 0) {
            argsValue = new String[]{named.idFor()};
        }
        final boolean useCache = named.useCache();
        final String namedType = named.type();
        final Object idValue = LangUtils.getObjectValue(target, idField, null);
        Class<?> serviceClass = LangUtils.computeIfAbsent(serviceClassMaps, target.getClass().getName(),
            s -> this.getServiceClassOrDefault(useService, NamedService.class));
        NamedRow namedRow = new NamedRow()
            .setUseCache(useCache)
            .setTarget(target)
            .setIdValue(idValue)
            .setArgs(argsValue)
            .setServiceClass(serviceClass)
            .setTargetField(targetField)
            .setNamedType(namedType);
        return Optional.of(namedRow);
    }

    private List<NamedRow> getArrayNamedRows(Object[] result) {
        return getCollectionNamedRows(Lists.newArrayList(result));
    }

    private List<NamedRow> getCollectionNamedRows(Collection<?> result) {
        return result.parallelStream().flatMap(o -> getNamedRows(o).parallelStream())
            .collect(Collectors.toList());
    }

    private void injectValue(List<NamedRow> namedRows) {
        injectValueWithCache(namedRows);
        injectValueWithMethod(namedRows);
    }

    private void injectValueWithCache(List<NamedRow> namedRows) {
        // 按缓存key进行分组
        Map<String, List<NamedRow>> namedRowMaps = namedRows.parallelStream()
            .filter(namedRow -> Objects.isNull(namedRow.getTargetValue()) && Objects.nonNull(namedRow.getIdValue()))
            .collect(Collectors.groupingBy(this::getCacheKey));

        if (CollUtil.isEmpty(namedRowMaps)) {
            return;
        }

        // 读取缓存内容
        Map<String, Object> keyValues = cacheService.batchGet(namedRowMaps.keySet());

        if (CollUtil.isEmpty(keyValues)) {
            return;
        }

        keyValues.entrySet().parallelStream().forEach(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            List<NamedRow> rNamedRows = namedRowMaps.getOrDefault(key, Collections.emptyList());
            rNamedRows.parallelStream().forEach(row -> setValue(row, value));
        });
    }

    private void injectValueWithMethod(List<NamedRow> namedRows) {
        // 按ID进行分组
        Map<Object, List<NamedRow>> idNamedRowGroup = namedRows.parallelStream()
            .filter(namedRow -> Objects.isNull(namedRow.getTargetValue()) && Objects.nonNull(namedRow.getIdValue()))
            .collect(Collectors.groupingBy(row -> String.valueOf(row.getIdValue())));

        if (CollUtil.isEmpty(idNamedRowGroup)) {
            return;
        }

        // 提取关键信息
        NamedRow namedRow = namedRows.get(0);
        String namedType = namedRow.getNamedType();
        Class<?> serviceClass = namedRow.getServiceClass();
        String[] args = namedRow.getArgs();
        Object[] ids = idNamedRowGroup.keySet().toArray();

        // 进行查询
        Map<String, Object> values = callNamedHandleMethod(serviceClass, namedType, ids, args);
        log.debug("===> {}-{}-{}::{}", namedType, ids, args, values);

        if (CollUtil.isEmpty(values)) {
            return;
        }

        // 进行翻译工作
        Map<String, Object> caches = Maps.newConcurrentMap();
        values.entrySet().parallelStream().forEach(entry -> {
            String idValue = entry.getKey();
            Object value = entry.getValue();
            if (Objects.isNull(value)) {
                return;
            }
            List<NamedRow> rNamedRows = idNamedRowGroup.getOrDefault(idValue, Collections.emptyList());
            rNamedRows.parallelStream().forEach(row -> {
                setValue(row, value);
                caches.put(getCacheKey(row), value);
            });
        });

        if (CollUtil.isEmpty(caches)) {
            return;
        }
        // 批量更新到缓存中..
        cacheService.batchPut(caches);
    }

    private void setValue(NamedRow namedRow, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        namedRow.setTargetValue(value);
        LangUtils.setFieldValue(namedRow.getTarget(), namedRow.getTargetField(), value);
    }

    private String getGroupKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        String[] args = namedRow.getArgs();
        return namedType + "@" + Arrays.toString(args);
    }

    private String getCacheKey(NamedRow namedRow) {
        return cacheService.getCacheKey(namedRow);
    }

    private Map<String, Object> callNamedHandleMethod(Class<?> serviceClass, String namedType, Object[] ids, String[] args) {
        final Object namedService = context.getBean(serviceClass);

        Method method = LangUtils.computeIfAbsent(serviceMethodMaps, StrUtil.format("{}-{}", serviceClass.getName(), namedType),
            (s) -> this.getNamedHandlerMethod(serviceClass, namedType).orElse(null));

        if (Objects.isNull(method)) {
            return Collections.emptyMap();
        }

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
            log.warn("服务调用失败, 请检查参数 @Named 提供者[{}], 函数[{}], 参数[{}]", namedService, method, JSONUtil.toJsonStr(namedArgs), e);
            return Collections.emptyMap();
        }
    }


    /**
     * 获取 Service Class 默认为 NamedService.class 的实现类
     *
     * @param useNamedService _
     * @param defServiceClass _
     * @return _
     */
    private Class<?> getServiceClassOrDefault(UseNamedService useNamedService, Class<?> defServiceClass) {
        if (Objects.nonNull(useNamedService)) {
            return useNamedService.value();
        }

        try {
            Class<?> beanClass = context.getBeansOfType(defServiceClass).values().stream().findFirst()
                .map((Function<Object, Class<?>>) Object::getClass).orElse(defServiceClass);
            if (defServiceClass.isAssignableFrom(beanClass)) {
                return beanClass;
            }
        } catch (Exception e) {
            log.debug("@Named 自动获取 serviceClass 异常", e);
        }
        return defServiceClass;
    }

    /**
     * 获取处理函数
     *
     * @param serviceClass _
     * @param namedType    _
     * @return _
     */
    private Optional<Method> getNamedHandlerMethod(Class<?> serviceClass, String namedType) {
        return Arrays.stream(serviceClass.getMethods()).parallel()
            .filter(method -> {
                final NamedHandler annotation = method.getAnnotation(NamedHandler.class);
                if (Objects.isNull(annotation)) {
                    return false;
                }
                return annotation.value().equals(namedType);
            }).findAny();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheService = context.getBean(NamedCacheService.class);
    }
}
