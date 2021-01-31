package in.hocg.boot.named.autoconfiguration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hocgin on 2020/2/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseNamedService {

    /**
     * 服务提供类
     *
     * @return
     */
    Class<?> value() default NamedService.class;

}
