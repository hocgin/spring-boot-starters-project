package in.hocg.boot.utils.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by hocgin on 2020/4/10.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@FunctionalInterface
public interface SFunction <T, R> extends Function<T, R>, Serializable {
}
