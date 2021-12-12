package in.hocg.boot.utils.lambda.map;


import in.hocg.boot.utils.PropertyNamer;
import in.hocg.boot.utils.lambda.SFunction;
import in.hocg.boot.utils.lambda.SerializedLambda;

import java.util.Map;

/**
 * Created by hocgin on 2020/6/7.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface LambdaMap<V> extends Map<String, V> {

    default <T> V put(SFunction<T, ?> keyFunction, V value) {
        return this.put(key(keyFunction), value);
    }

    default <T> V get(SFunction<T, ?> keyFunction) {
        return this.get(key(keyFunction));
    }

    default <T> String getAsString(SFunction<T, ?> keyFunction) {
        return ((String) this.get(key(keyFunction)));
    }

    default <T> Integer getAsInt(SFunction<T, ?> keyFunction) {
        return ((Integer) this.get(key(keyFunction)));
    }

    default <T> V getOrDefault(SFunction<T, ?> keyFunction, V defaultValue) {
        return this.getOrDefault(key(keyFunction), defaultValue);
    }

    default <T> V remove(SFunction<T, ?> keyFunction) {
        return this.remove(key(keyFunction));
    }

    default <T> String key(SFunction<T, ?> keyFunction) {
        return PropertyNamer.methodToProperty(SerializedLambda.resolve(keyFunction).getImplMethodName());
    }

}
