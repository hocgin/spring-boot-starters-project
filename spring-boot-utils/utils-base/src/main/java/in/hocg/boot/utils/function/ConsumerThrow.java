package in.hocg.boot.utils.function;

/**
 * Created by hocgin on 2022/4/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@FunctionalInterface
public interface ConsumerThrow<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws Exception;
}
