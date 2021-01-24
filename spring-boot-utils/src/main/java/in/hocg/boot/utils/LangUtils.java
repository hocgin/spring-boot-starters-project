package in.hocg.boot.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2020/8/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class LangUtils {
    public boolean isPresent(String name) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean equals(Integer s1, Integer s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.compareTo(s2) == 0;
    }

    public boolean equals(Long s1, Long s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.compareTo(s2) == 0;
    }

    public boolean equals(Boolean s1, Boolean s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.equals(s2);
    }

    public boolean equals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.equals(s2);
    }

    /**
     * 数组 转为 Map
     *
     * @param values      values
     * @param keyFunction keyFunction
     * @param <K>         any
     * @param <V>         any
     * @return Map
     */
    public <K, V> Map<K, V> toMap(List<V> values, Function<? super V, K> keyFunction) {
        Map<K, V> result = new HashMap<>();
        for (V val : values) {
            K key = keyFunction.apply(val);
            result.put(key, val);
        }
        return result;
    }

    public <K, V, Z> Map<K, Z> toMap(List<V> values,
                                     Function<? super V, K> keyFunction,
                                     Function<? super V, Z> valueFunction) {
        Map<K, Z> result = new HashMap<>();
        for (V val : values) {
            K key = keyFunction.apply(val);
            Z value = valueFunction.apply(val);
            result.put(key, value);
        }
        return result;
    }

    /**
     * 获取对象字段的值
     *
     * @param fieldObject fieldObject
     * @param field       field
     * @param def         def
     * @return object
     */
    public Object getFieldValue(Object fieldObject, Field field,
                                Object def) {
        if (Objects.isNull(fieldObject)
            || Objects.isNull(field)) {
            return def;
        }

        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(fieldObject);
        } catch (IllegalAccessException ignored) {
            return def;
        } finally {
            field.setAccessible(accessible);
        }
    }

    public <T> T getObjectValue(Object fieldObject, Field field,
                                Object def) {
        return ((T) getFieldValue(fieldObject, field, def));
    }

    /**
     * 设置对象某字段的值
     *
     * @param fieldObject fieldObject
     * @param field       field
     * @param value       value
     */
    public void setFieldValue(Object fieldObject, Field field, Object value) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(fieldObject, value);
        } catch (IllegalAccessException ignored) {
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * 获取所有字段
     *
     * @return []
     */
    public List<Field> getAllField(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        result.addAll(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superclass = clazz.getSuperclass();
        if (Object.class.equals(superclass)) {
            return result;
        }
        result.addAll(getAllField(superclass));
        return result;
    }

    /**
     * 判断是否基础类型
     *
     * @param clazz clazz
     * @return bool
     */
    public boolean isBaseType(Class<?> clazz) {
        return clazz.equals(Integer.class) ||
            clazz.equals(Byte.class) ||
            clazz.equals(Long.class) ||
            clazz.equals(Double.class) ||
            clazz.equals(Float.class) ||
            clazz.equals(Character.class) ||
            clazz.equals(Short.class) ||
            clazz.equals(Boolean.class);
    }

    public String toString(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        if (object instanceof Integer) {
            return Integer.toString((Integer) object);
        } else if (object instanceof String) {
            return ((String) object);
        } else {
            return object.toString();
        }
    }

    public List<String> toList(Object[] arr) {
        List<String> result = new ArrayList<>();
        for (Object i : arr) {
            result.add(toString(i));
        }
        return result;
    }

    /**
     * 提取 List 项的值
     *
     * @param values
     * @param keyFunction
     * @param <V>
     * @param <R>
     * @return
     */
    public <V, R> List<R> toList(Iterable<V> values, Function<? super V, R> keyFunction) {
        List<R> result = Lists.newArrayList();
        for (V val : values) {
            result.add(keyFunction.apply(val));
        }
        return result;
    }

    /**
     * 移除 all 中的 sub
     * all - sub
     *
     * @param all
     * @param sub
     * @param biFunction
     * @param <R>
     * @param <T>
     * @return
     */
    public <R, T> List<R> removeIfExits(Collection<R> all, Collection<T> sub, BiFunction<R, T, Boolean> biFunction) {
        List<R> result = Lists.newArrayList(all);
        if (result.isEmpty()) {
            return result;
        }
        final Iterator<R> iterator = result.iterator();
        while (iterator.hasNext()) {
            final R r = iterator.next();
            for (T t : sub) {
                if (biFunction.apply(r, t)) {
                    iterator.remove();
                }
            }
        }
        return result;
    }

    /**
     * 获取两个 List 的交集
     *
     * @param l1
     * @param l2
     * @param biFunction
     * @param <R>
     * @param <T>
     * @return
     */
    public <R, T> List<R> getMixed(Collection<R> l1, Collection<T> l2, BiFunction<R, T, Boolean> biFunction) {
        List<R> result = Lists.newArrayList();
        for (R r : l1) {
            for (T t : l2) {
                if (biFunction.apply(r, t)) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    /**
     * 获取如果为 NULL，则返回默认值
     *
     * @param v
     * @param def
     * @param <T>
     * @return
     */
    public <T> T getOrDefault(T v, T def) {
        if (Objects.isNull(v)) {
            return def;
        }
        return v;
    }

    /**
     * 如果传入的值不为 NULL，则当作入参执行后续函数
     *
     * @param v
     * @param func
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Optional<V> callIfNotNull(K v, Function<K, V> func) {
        if (Objects.nonNull(v)) {
            return Optional.ofNullable(func.apply(v));
        }
        return Optional.empty();
    }

    /**
     * 如果传入的值不为 NULL，则当作入参执行后续函数
     *
     * @param v
     * @param consumer
     * @param>
     */
    public <T> void setIfNotNull(T v, Consumer<T> consumer) {
        if (Objects.nonNull(v)) {
            consumer.accept(v);
        }
    }

    /**
     * 分组调用
     * - 将大批量数据，分组后，在整合
     *
     * @param allIds
     * @param queryFunction
     * @param len
     * @param <R>
     * @param <P>
     * @return
     */
    public <R, P> List<R> groupCallback(Collection<P> allIds,
                                        Function<Collection<P>, Collection<R>> queryFunction, int len) {
        if (CollectionUtil.isEmpty(allIds)) {
            return Collections.emptyList();
        }
        List<P> all = Lists.newArrayList(allIds);
        List<R> result = Lists.newArrayList();

        int startIndex = 0;
        final int maxLength = all.size();

        while (startIndex < maxLength) {
            int toIndex = Math.min((startIndex + len), maxLength);

            final List<P> ids = all.subList(startIndex, toIndex);
            result.addAll(queryFunction.apply(ids));
            startIndex = toIndex;
        }
        return result;
    }
}
