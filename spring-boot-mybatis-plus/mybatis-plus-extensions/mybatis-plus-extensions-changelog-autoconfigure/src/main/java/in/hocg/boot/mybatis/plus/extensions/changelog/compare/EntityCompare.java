package in.hocg.boot.mybatis.plus.extensions.changelog.compare;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.FieldChangeDto;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.utils.PropertyNamer;
import in.hocg.boot.utils.lambda.SFunction;
import in.hocg.boot.utils.lambda.SerializedLambda;
import io.swagger.annotations.ApiModelProperty;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2020/4/10.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class EntityCompare {

    public static <T> List<FieldChangeDto> diffLambdaNotNull(@NonNull T oldValue, @NonNull T newValue, String... fields) {
        List<String> ignoreFieldNames = LangUtils.callIfNotNull(fields, Lists::newArrayList).orElse(Lists.newArrayList());
        return EntityCompare.diff(oldValue, newValue, true, ignoreFieldNames);
    }

    public static <T> List<FieldChangeDto> diffNotNull(@NonNull T oldValue, @NonNull T newValue, SFunction<T, ?>... fields) {
        return EntityCompare.diffLambda(oldValue, newValue, true, fields);
    }

    public static <T> List<FieldChangeDto> diffNotNull(@NonNull T newValue, SFunction<T, ?>... fields) {
        return diff(newValue, true, fields);
    }

    /**
     * 比较
     *
     * @param oldValue
     * @param newValue
     * @param ignoreNull
     * @param fields
     * @return
     */
    public static <T> List<FieldChangeDto> diffLambda(@NonNull T oldValue, @NonNull T newValue, boolean ignoreNull, SFunction<T, ?>... fields) {
        List<String> fieldNames = Collections.emptyList();
        if (Objects.nonNull(fields)) {
            fieldNames = Arrays.stream(fields).map(func -> PropertyNamer.methodToProperty(SerializedLambda.resolve(func).getImplMethodName()))
                .collect(Collectors.toList());
        }
        return EntityCompare.diff(oldValue, newValue, ignoreNull, fieldNames);
    }

    public static <T> List<FieldChangeDto> diff(@NonNull T newValue, boolean ignoreNull, SFunction<T, ?>... fields) {
        T obj = (T) ReflectUtil.newInstance(newValue.getClass());
        return diffLambda(obj, newValue, ignoreNull, fields);
    }

    /**
     * 对比记录变更的字段
     *
     * @param oldValue     旧的值
     * @param newValue     新的值
     * @param ignoreNull   是否忽略 NULL
     * @param ignoreFields 需忽略的字段名
     * @return
     */
    public static <T> List<FieldChangeDto> diff(@NonNull T oldValue, @NonNull T newValue, boolean ignoreNull,
                                                List<String> ignoreFields) {
        final List<FieldChangeDto> result = Lists.newArrayList();
        final Class<?> nClass = newValue.getClass();
        final Class<?> oClass = oldValue.getClass();
        final Map<String, Field> oFieldMaps = ReflectUtil.getFieldMap(oClass);
        final List<Field> nFields = Lists.newArrayList(ReflectUtil.getFields(nClass));
        for (Field nField : nFields) {
            final String nFieldName = nField.getName();

            // 如果需要忽略字段名
            if (CollUtil.isNotEmpty(ignoreFields) && ignoreFields.contains(nFieldName)) {
                continue;
            }

            final Object nFieldValue = ReflectUtil.getFieldValue(newValue, nField);
            // 如果需要忽略 NULL 值
            if (ignoreNull && Objects.isNull(nFieldValue)) {
                continue;
            }
            final Field oField = oFieldMaps.get(nFieldName);
            final Object oFieldValue = ReflectUtil.getFieldValue(oldValue, oField);
            final String after = String.valueOf(nFieldValue);
            final String before = String.valueOf(oFieldValue);

            // 如果两个值一样
            if (LangUtils.equals(after, before)) {
                continue;
            }

            result.add(new FieldChangeDto()
                .setFieldRemark(getFieldRemark(nField))
                .setFieldName(nFieldName)
                .setChangeRemark(String.format("%s: %s -> %s", nFieldName, before, after))
                .setAfterValue(after)
                .setBeforeValue(before));
        }
        return result;
    }

    /**
     * 获取字段备注
     *
     * @param field
     * @return
     */
    private static String getFieldRemark(Field field) {
        final boolean hasApiModeProperty = field.isAnnotationPresent(ApiModelProperty.class);
        if (hasApiModeProperty) {
            final ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            return annotation.value();
        }
        return String.format("%s属性", field.getName());
    }
}
