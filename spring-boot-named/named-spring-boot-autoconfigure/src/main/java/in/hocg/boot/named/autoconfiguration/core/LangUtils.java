package in.hocg.boot.named.autoconfiguration.core;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by hocgin on 2020/1/5.
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
        ArrayList<Field> result = new ArrayList<>();
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
        List<String> result = Lists.newArrayList();
        for (Object i : arr) {
            result.add(toString(i));
        }
        return result;
    }
}
