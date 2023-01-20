package in.hocg.boot.utils.ext;

import in.hocg.boot.utils.utils.LambdaUtils;
import in.hocg.boot.utils.lambda.SFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2022/12/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MultiList<T> {
    private final Map<String, List<Object>> maps;

    public MultiList() {
        this.maps = new HashMap<>();
    }

    public MultiList<T> add(SFunction<T, ?> fn, Object value) {
        String listKey = LambdaUtils.getPropertyName(fn);
        List<Object> values = maps.computeIfAbsent(listKey, s -> new ArrayList<>());
        values.add(value);
        return this;
    }

    public <R> List<R> get(SFunction<T, ?> fn) {
        return (List<R>) maps.getOrDefault(getKey(fn), Collections.emptyList());
    }

    private String getKey(SFunction<T, ?> fn) {
        return LambdaUtils.getPropertyName(fn);
    }

}
