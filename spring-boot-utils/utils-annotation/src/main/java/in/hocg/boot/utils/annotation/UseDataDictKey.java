package in.hocg.boot.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hocgin on 2021/12/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseDataDictKey {

    String value() default "";
}
