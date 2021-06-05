package in.hocg.boot.named.autoconfiguration.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.boot.named.annotation.NamedService;
import in.hocg.boot.named.annotation.UseNamedService;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.NamedRow;
import in.hocg.boot.named.autoconfiguration.core.convert.NamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.utils.NamedUtils;
import in.hocg.boot.named.ifc.NamedArgs;
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
import java.util.concurrent.ConcurrentLinkedQueue;
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
     * Named.field, ServiceClass
     */
    private final Map<String, Object> namedServiceClassMaps = Maps.newConcurrentMap();

    @Pointcut("@within(org.springframework.stereotype.Service) && execution((*) *(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return handleResult(point.proceed());
    }

    private Object handleResult(Object result) {
        Stopwatch started = Stopwatch.createStarted();
        List<NamedRow> namedRows = getNamedRows(result);
        log.debug("查找需要匹配的 @Named 数量: {} " + started.toString(), namedRows.size());

        if (CollectionUtils.isEmpty(namedRows)) {
            return result;
        }
        Map<String, List<NamedRow>> namedGroup = namedRows.parallelStream().collect(Collectors.groupingBy(this::getGroupKey));
        log.debug("@Named 分组数量: {} " + started.toString(), namedGroup.size());

        namedGroup.values().parallelStream().forEach(this::injectValue);
        log.debug("@Named 处理完成: {} " + started.stop().toString(), namedGroup.size());
        return result;
    }

    private boolean isSupportClass(Class<?> aClass) {
        return Arrays.class.isAssignableFrom(aClass)
            || Collection.class.isAssignableFrom(aClass)
            || aClass.isAnnotationPresent(InjectNamed.class)
            || this.converts.stream().anyMatch(convert -> convert.isMatch(aClass));
    }

    protected List<NamedRow> getNamedRows(Object result) {
        if (Objects.isNull(result) || !this.isSupportClass(result.getClass())) {
            return Collections.emptyList();
        }

        if (result instanceof Collection) {
            return getCollectionNamedRows((Collection<?>) result);
        } else if (result instanceof Object[]) {
            return getArrayNamedRows((Object[]) result);
        }

        // 扩展点
        Optional<NamedRowsConvert> convertOpt = this.converts.stream()
            .filter(namedRowsConvert -> namedRowsConvert.isMatch(result.getClass())).findFirst();
        if (convertOpt.isPresent()) {
            NamedRowsConvert convert = convertOpt.get();
            Object tResult = convertOpt.get().convert(result);
            if (Objects.isNull(tResult) || tResult.getClass().equals(result.getClass())) {
                log.warn("在 @Named 扩展了非法转换器[{}], 请及时更正", convert.getClass());
                return Collections.emptyList();
            }
            return getNamedRows(tResult);
        }

        return getObjectNamedRows(result);
    }

    private List<NamedRow> getObjectNamedRows(Object result) {
        // 解析对象的字段
        ConcurrentLinkedQueue<NamedRow> namedRows = new ConcurrentLinkedQueue<>();
        Map<String, Field> fieldMap = NamedUtils.getAllField(result.getClass());
        fieldMap.values().parallelStream().forEach(field -> {
            if (field.isAnnotationPresent(Named.class)) {
                getNamedRow(result, fieldMap, field).ifPresent(namedRows::add);
            } else if (isSupportClass(field.getType())) {
                final Object value = NamedUtils.getFieldValue(result, field);
                namedRows.addAll(getNamedRows(value));
            }
        });
        return Lists.newArrayList(namedRows);
    }

    /**
     * 获取需要
     *
     * @param target      当前对象
     * @param fieldMap    当前对象的所有字段
     * @param targetField 等待@Named的对象
     * @return
     */
    private Optional<NamedRow> getNamedRow(Object target, Map<String, Field> fieldMap, Field targetField) {
        final Named named = targetField.getAnnotation(Named.class);
        final Field idField = fieldMap.get(named.idFor());
        if (Objects.isNull(idField)) {
            return Optional.empty();
        }
        Object serviceBean = LangUtils.computeIfAbsent(namedServiceClassMaps, StrUtil.format("{}.{}", target.getClass(), targetField.getName()),
            s -> this.getServiceBeanOrDefault(targetField.getAnnotation(UseNamedService.class), NamedService.class));
        return Optional.of(new NamedRow()
            .setUseCache(named.useCache())
            .setTarget(target)
            .setIdValue(NamedUtils.getFieldValue(target, idField))
            .setArgs(named.args())
            .setServiceObject(serviceBean)
            .setTargetField(targetField)
            .setNamedType(named.type()));
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

    /**
     * 从缓存中读取
     *
     * @param namedRows
     */
    private void injectValueWithCache(List<NamedRow> namedRows) {
        Map<String, List<NamedRow>> namedRowMaps = namedRows.parallelStream()
            .filter(NamedRow::getUseCache)
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
            namedRowMaps.getOrDefault(key, Collections.emptyList()).parallelStream().forEach(row -> {
                setValue(row, value);
            });
        });
    }

    /**
     * 从 Service 中读取
     *
     * @param namedRows
     */
    private void injectValueWithMethod(List<NamedRow> namedRows) {
        Map<Object, List<NamedRow>> idNamedRowGroup = namedRows.parallelStream()
            .filter(namedRow -> Objects.isNull(namedRow.getTargetValue()) && Objects.nonNull(namedRow.getIdValue()))
            .collect(Collectors.groupingBy(row -> String.valueOf(row.getIdValue())));
        if (CollUtil.isEmpty(idNamedRowGroup)) {
            return;
        }

        // 提取关键信息
        NamedRow namedRow = namedRows.get(0);
        String namedType = namedRow.getNamedType();
        Object serviceObject = namedRow.getServiceObject();
        String[] args = namedRow.getArgs();
        Object[] ids = idNamedRowGroup.keySet().toArray();
        Map<String, Object> values = callNamedHandleMethod(serviceObject, namedType, ids, args);
        log.debug("===> {}-{}-{}::{}", namedType, ids, args, values);
        if (CollUtil.isEmpty(values)) {
            return;
        }

        // 进行翻译工作
        Map<String, Object> caches = Maps.newConcurrentMap();
        values.entrySet().parallelStream().filter(entry -> Objects.nonNull(entry.getValue())).forEach(entry -> {
            Object value = entry.getValue();
            idNamedRowGroup.getOrDefault(entry.getKey(), Collections.emptyList()).parallelStream().forEach(row -> {
                setValue(row, value);
                caches.put(getCacheKey(row), value);
            });
        });

        if (CollUtil.isNotEmpty(caches)) {
            // 批量更新到缓存中..
            cacheService.batchPut(caches);
        }
    }

    private void setValue(NamedRow namedRow, Object value) {
        if (Objects.isNull(value)) {
            return;
        }
        namedRow.setTargetValue(value);
        NamedUtils.setFieldValue(namedRow.getTarget(), namedRow.getTargetField(), value);
    }

    private String getGroupKey(NamedRow namedRow) {
        String namedType = namedRow.getNamedType();
        String[] args = namedRow.getArgs();
        Class<?> serviceClass = namedRow.getServiceObject().getClass();
        return StrUtil.format("{}@{}@{}", serviceClass.getName(), namedType, Arrays.toString(args));
    }

    private String getCacheKey(NamedRow namedRow) {
        return cacheService.getCacheKey(namedRow);
    }

    private Map<String, Object> callNamedHandleMethod(Object namedService, String namedType, Object[] ids, String[] args) {
        Method method = NamedUtils.getNamedHandleMethod(namedService.getClass(), namedType);
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
    private Object getServiceBeanOrDefault(UseNamedService useNamedService, Class<?> defServiceClass) {
        if (Objects.nonNull(useNamedService)) {
            return context.getBean(useNamedService.value());
        }
        return context.getBean(defServiceClass);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        cacheService = context.getBean(NamedCacheService.class);
    }
}
