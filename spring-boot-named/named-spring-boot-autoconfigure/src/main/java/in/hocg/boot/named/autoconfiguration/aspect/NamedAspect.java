package in.hocg.boot.named.autoconfiguration.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.boot.named.autoconfiguration.core.CachePool;
import in.hocg.boot.named.autoconfiguration.core.NamedCacheService;
import in.hocg.boot.named.autoconfiguration.core.NamedRow;
import in.hocg.boot.named.autoconfiguration.core.convert.NamedRowsConvert;
import in.hocg.boot.named.autoconfiguration.properties.NamedProperties;
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
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
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
    private final NamedProperties properties;
    private NamedCacheService cacheService;

    @Pointcut("@within(org.springframework.stereotype.Service) && execution((*) *(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Stopwatch started = null;
        if (log.isDebugEnabled()) {
            started = Stopwatch.createStarted();
            log.debug("=> @Named 调用开始, 时间: [{}]", started);
        }
        try {
            NamedContext.push();
            Object proceed = point.proceed();
            if (log.isDebugEnabled()) {
                log.debug("=> @Named Service 层处理完成, 时间: [{}]", started);
            }
            return handleResult(proceed);
        } finally {
            NamedContext.pop();
            if (log.isDebugEnabled()) {
                log.debug("=> @Named 调用结束, 时间: [{}]", LangUtils.callIfNotNull(started, Stopwatch::stop).orElse(null));
            }
        }
    }

    public Object handleResult(Object result) {
        Stopwatch started = null;
        if (log.isDebugEnabled()) {
            started = Stopwatch.createStarted();
        }
        List<NamedRow> namedRows = getNamedRows(result);
        if (log.isDebugEnabled()) {
            log.debug("1. 查找需要匹配的 @Named 数量: [{}], 时间: [{}]", namedRows.size(), started);
        }

        if (CollectionUtils.isEmpty(namedRows)) {
            return result;
        }

        Map<String, List<NamedRow>> namedGroup = namedRows.parallelStream().collect(Collectors.groupingBy(this::getGroupKey));

        if (log.isDebugEnabled()) {
            log.debug("2. @Named 分组数量: [{}], 时间: [{}]", namedGroup.size(), started);
        }

        namedGroup.values().parallelStream().forEach(this::injectValue);
        if (log.isDebugEnabled()) {
            log.debug("3. @Named 处理完成: [{}], 时间: [{}]", namedGroup.size(), LangUtils.callIfNotNull(started, Stopwatch::stop).orElse(null));
        }
        return result;
    }

    private boolean isSupportClass(Class<?> aClass) {
        return CachePool.isSupportNamed(aClass, clazz ->
            Arrays.class.isAssignableFrom(clazz)
                || Collection.class.isAssignableFrom(clazz)
                || clazz.isAnnotationPresent(InjectNamed.class)
                || this.converts.stream().anyMatch(convert -> convert.isMatch(clazz)));
    }

    protected List<NamedRow> getNamedRows(Object result) {
        if (!NamedContext.add(NamedContext.id(result))) {
            return Collections.emptyList();
        }
        // =====================================================================
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
            Object tResult = convert.convert(result);
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
            if (AnnotatedElementUtils.hasAnnotation(field, Named.class)) {
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
        final Named named = AnnotatedElementUtils.getMergedAnnotation(targetField, Named.class);
        final Field idField = fieldMap.get(named.idFor());
        if (Objects.isNull(idField)) {
            return Optional.empty();
        }

        Class<?> nameServiceClass = named.useService();
        Object serviceBean = this.getNamedServiceClassMaps().get(nameServiceClass);
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
        Map<String, List<NamedRow>> namedRowMaps = namedRows.stream()
            .filter(NamedRow::getUseCache)
            .filter(namedRow -> Objects.isNull(namedRow.getTargetValue()) && Objects.nonNull(namedRow.getIdValue()))
            .collect(Collectors.groupingBy(this::getCacheKey));
        if (CollUtil.isEmpty(namedRowMaps)) {
            return;
        }

        // 读取缓存内容
        Map<String, Object> values = cacheService.batchGet(namedRowMaps.keySet());
        if (CollUtil.isEmpty(values)) {
            return;
        }

        values.entrySet().parallelStream().filter(entry -> Objects.nonNull(entry.getValue())).forEach(entry ->
            namedRowMaps.getOrDefault(entry.getKey(), Collections.emptyList()).forEach(row -> setValue(row, entry.getValue()))
        );
    }

    /**
     * 从 Service 中读取
     *
     * @param namedRows
     */
    private void injectValueWithMethod(List<NamedRow> namedRows) {
        Map<Object, List<NamedRow>> idNamedRowGroup = namedRows.stream()
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
        Map<String, Object> values = this.callNamedHandleMethod(serviceObject, namedType, ids, args);
        log.debug("===> {}-{}-{}::{}", namedType, ids, args, values);
        if (CollUtil.isEmpty(values)) {
            return;
        }

        // 进行翻译工作
        Map<String, Object> caches = Maps.newConcurrentMap();
        values.entrySet().parallelStream().filter(entry -> Objects.nonNull(entry.getValue())).forEach(entry ->
            idNamedRowGroup.getOrDefault(entry.getKey(), Collections.emptyList()).forEach(row -> caches.put(getCacheKey(row), setValue(row, entry.getValue())))
        );

        if (CollUtil.isNotEmpty(caches)) {
            // 批量更新到缓存中..
            cacheService.batchPut(caches);
        }
    }

    private Object setValue(NamedRow namedRow, Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        namedRow.setTargetValue(value);
        NamedUtils.setFieldValue(namedRow.getTarget(), namedRow.getTargetField(), value);
        return value;
    }

    private String getGroupKey(NamedRow namedRow) {
        return StrUtil.format("{}@{}", namedRow.getNamedType(), Arrays.toString(namedRow.getArgs()));
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
                return (Map) invokeResult;
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            log.warn("服务调用失败, 请检查参数 @Named 提供者[{}], 函数[{}], 参数[{}]", namedService, method, JSONUtil.toJsonStr(namedArgs), e);
            return Collections.emptyMap();
        }
    }

    public Map<Class<?>, Object> getNamedServiceClassMaps() {
        return CachePool.NAMED_SERVICE_CLASS_MAPS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheService = context.getBean(NamedCacheService.class);
        try {
            CachePool.load(context);
        } catch (Exception e) {
            log.warn("@Named 预缓存发生错误", e);
        }
    }
}
