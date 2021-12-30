package in.hocg.boot.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import in.hocg.boot.utils.annotation.UseDataDictKey;
import in.hocg.boot.utils.dto.DictData;
import in.hocg.boot.utils.enums.DataDictEnum;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
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
     * 扫描获取数据字典类
     *
     * @param basePackages 包路径
     * @return
     */
    public List<Class<?>> scanClass(String... basePackages) {
        return Arrays.stream(basePackages).parallel()
            .flatMap(s -> ClassUtil.scanPackageBySuper(s, DataDictEnum.class).parallelStream())
            .filter(Class::isEnum).collect(Collectors.toList());
    }

    /**
     * 扫描所有实现 DataDictEnum 的枚举类并缓存
     *
     * @param basePackages 扫描的包路径(可以为父级)
     * @return 扫描结果
     */
    public Map<String, Map<String, Object>> scanMaps(String... basePackages) {
        List<DictData> all = scan(basePackages);
        if (ENUM_CACHE.isEmpty()) {
            for (DictData data : all) {
                String key = data.getCode();
                List<DictData.Item> items = data.getItems();
                Map<String, Object> map = new HashMap<>(items.size());
                for (DictData.Item item : items) {
                    map.put(StrUtil.toString(item.getCode()), item.getTitle());
                }
                ENUM_CACHE.put(key, map);
            }
        }
        return ENUM_CACHE;
    }

    /**
     * 扫描为数据结构
     *
     * @param basePackages
     * @return
     */
    public List<DictData> scan(String... basePackages) {
        List<Class<?>> classes = scanClass(basePackages);
        List<DictData> result = Lists.newArrayList();
        for (Class<?> clazz : classes) {
            DictData dataDict = new DictData();
            String key = clazz.getSimpleName();
            String title = clazz.getSimpleName() + "未使用 @UseDataDictKey";
            boolean enabled = clazz.isAnnotationPresent(Deprecated.class);
            if (clazz.isAnnotationPresent(UseDataDictKey.class)) {
                UseDataDictKey atn = clazz.getAnnotation(UseDataDictKey.class);
                key = atn.value();
                title = atn.description();
            }
            dataDict.setCode(key);
            dataDict.setTitle(title);
            dataDict.setEnabled(enabled);

            // 字典项
            DataDictEnum[] enums = (DataDictEnum[]) clazz.getEnumConstants();
            final List<DictData.Item> items = Lists.newArrayListWithExpectedSize(enums.length);
            for (DataDictEnum itemEnum : enums) {
                boolean isDeprecated = ClassUtils.getField(clazz, ((Enum<?>) itemEnum).name()).orElseThrow(IllegalArgumentException::new)
                    .isAnnotationPresent(Deprecated.class);
                items.add(new DictData.Item().setTitle(itemEnum.getName()).setEnabled(isDeprecated).setCode(itemEnum.getCode()));
            }
            dataDict.setItems(items);
            result.add(dataDict);
        }
        return result;
    }

    /**
     * 扫描获取生成SQL
     *
     * @param basePackages
     * @param formatDataDict
     * @param formatDataDictItem
     * @return
     */
    public List<String> scanSql(List<String> basePackages, Function<DictData, String> formatDataDict, BiFunction<DictData, DictData.Item, String> formatDataDictItem) {
        List<DictData> all = scan(basePackages.toArray(new String[]{}));
        List<String> result = Lists.newArrayList();
        for (DictData data : all) {
            result.add(formatDataDict.apply(data));
            for (DictData.Item item : data.getItems()) {
                result.add(formatDataDictItem.apply(data, item));
            }
        }
        return result;
    }

    /**
     * 扫描获取生成默认格式 SQL
     *
     * @param basePackages
     * @return
     */
    public List<String> scanSql(List<String> basePackages) {
        final String dataDictSql = "INSERT INTO com_data_dict(`title`, `code`, `remark`, `enabled`) VALUES ('{title}', '{code}', '{remark}', {enabled});";
        final String dataDictItemSql = "INSERT INTO com_data_dict_item(`dict_id`, `title`, `code`, `remark`, `enabled`) VALUES (@dict_id, '{title}', '{code}', '{remark}', {enabled});";
        return scanSql(basePackages, item -> {
            Map<String, String> vars = Maps.newHashMap();
            vars.put("title", item.getTitle());
            vars.put("code", item.getCode());
            vars.put("remark", item.getTitle());
            vars.put("enabled", item.getEnabled() ? "1" : "0");
            return Joiner.on(System.lineSeparator())
                .join(StrUtil.format(dataDictSql, vars), "set @dict_id := last_insert_id();");
        }, (data, item) -> {
            Map<String, String> vars = Maps.newHashMap();
            vars.put("title", item.getTitle());
            vars.put("code", StrUtil.toString(item.getCode()));
            vars.put("remark", StrUtil.format("{}#{}", data.getTitle(), item.getTitle()));
            vars.put("enabled", item.getEnabled() ? "1" : "0");
            return StrUtil.format(dataDictItemSql, vars);
        });
    }
}
