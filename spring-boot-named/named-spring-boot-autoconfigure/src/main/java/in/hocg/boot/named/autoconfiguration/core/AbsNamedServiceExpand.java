package in.hocg.boot.named.autoconfiguration.core;

import cn.hutool.core.convert.Convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/12/13
 * email: hocgin@gmail.com
 * 扩展
 *
 * @author hocgin
 */
public abstract class AbsNamedServiceExpand {

    /**
     * 获取参数值
     *
     * @param values _
     * @param clazz  _
     * @param <T>    _
     * @return _
     */
    protected <T> List<T> getValues(List<?> values, Class<T> clazz) {
        return values.stream().map(o -> Convert.convert(clazz, o)).collect(Collectors.toList());
    }

    /**
     * 转为 map
     *
     * @param values      _
     * @param keyHandle   _
     * @param valueHandle _
     * @param <K>         _
     * @param <V>         _
     * @param <Z>         _
     * @return _
     */
    protected <K, V, Z> Map<String, Z> toMap(List<V> values, Function<? super V, K> keyHandle, Function<? super V, Z> valueHandle) {
        Map<String, Z> result = new HashMap<>(values.size());
        for (V val : values) {
            String key = Convert.toStr(keyHandle.apply(val));
            Z value = valueHandle.apply(val);
            result.put(key, value);
        }
        return result;
    }
}
