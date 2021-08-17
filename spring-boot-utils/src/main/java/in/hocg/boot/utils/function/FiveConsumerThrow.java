package in.hocg.boot.utils.function;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@FunctionalInterface
public interface FiveConsumerThrow<T, U, Z, X, L> {
    void accept(T t, U u, Z z, X x, L l) throws Exception;
}
