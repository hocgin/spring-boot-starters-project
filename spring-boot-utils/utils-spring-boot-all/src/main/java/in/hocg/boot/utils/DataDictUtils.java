package in.hocg.boot.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.annotation.UseDataDictKey;
import in.hocg.boot.utils.enums.DataDictEnum;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/12/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class DataDictUtils {
    private static final Map<String, Map<String, Object>> ENUM_CACHE = new HashMap<>();

    /**
     * 扫描所有实现 DataDictEnum 的枚举类并缓存
     *
     * @param basePackages 扫描的包路径(可以为父级)
     * @return 扫描结果
     */
    public Map<String, Map<String, Object>> scan(String... basePackages) {
        if (!StrUtil.hasBlank(basePackages) && ENUM_CACHE.isEmpty()) {
            List<Class<?>> allClasses = Arrays.stream(basePackages).parallel()
                .flatMap(s -> ClassUtil.scanPackageBySuper(s, DataDictEnum.class).parallelStream())
                .filter(Class::isEnum)
                .collect(Collectors.toList());
            for (Class<?> clazz : allClasses) {
                DataDictEnum[] enums = (DataDictEnum[]) clazz.getEnumConstants();
                String key = clazz.getSimpleName();
                if (clazz.isAnnotationPresent(UseDataDictKey.class)) {
                    UseDataDictKey atn = clazz.getAnnotation(UseDataDictKey.class);
                    key = atn.value();
                }
                Map<String, Object> map = new HashMap<>(enums.length);
                for (DataDictEnum item : enums) {
                    map.put(StrUtil.toString(item.getCode()), item.getName());
                }
                ENUM_CACHE.put(key, map);
            }
        }
        return ENUM_CACHE;
    }

}
