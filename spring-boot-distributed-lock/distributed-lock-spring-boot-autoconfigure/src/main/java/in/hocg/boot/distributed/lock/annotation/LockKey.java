package in.hocg.boot.distributed.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by hocgin on 2019-09-29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface LockKey {
}
