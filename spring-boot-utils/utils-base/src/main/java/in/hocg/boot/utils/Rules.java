package in.hocg.boot.utils;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by hocgin on 2021/5/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class Rules<K> {
    private final Map<Function<K, Boolean>, Supplier<?>> rules = Maps.newHashMap();
    private Supplier<?> defaultRule = UnsupportedOperationException::new;

    public static <K> Rules<K> create() {
        return new Rules<>();
    }

    public Rules<K> rule(Function<K, Boolean> match, Supplier<?> fs) {
        rules.put(match, fs);
        return this;
    }

    public Rules<K> rule(K type, Supplier<?> fs) {
        return this.rule(keyFunction(type), fs);
    }

    public Rules<K> defRule(Supplier<?> fs) {
        defaultRule = fs;
        return this;
    }

    public <R> Optional<R> of(K args) {
        Supplier<?> rule = defaultRule;
        for (Map.Entry<Function<K, Boolean>, Supplier<?>> entry : rules.entrySet()) {
            if (entry.getKey().apply(args)) {
                rule = entry.getValue();
                break;
            }
        }
        Object o = rule.get();
        if (o instanceof RuntimeException) {
            throw (RuntimeException) o;
        }
        return (Optional<R>) Optional.ofNullable(o);
    }

    private Function<K, Boolean> keyFunction(K type) {
        return k -> Objects.equals(k, type);
    }

    public static <P> Supplier<P> Runnable(Runnable fun) {
        return () -> {
            fun.run();
            return null;
        };
    }

    public static <P> Supplier<P> Supplier(Supplier<P> fun) {
        return fun;
    }

}
