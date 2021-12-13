package in.hocg.boot.utils.function;

/**
 * Created by hocgin on 2021/6/19
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@FunctionalInterface
public interface BiConsumerThrow<T, U> {

    void accept(T t, U u) throws Exception;

}
