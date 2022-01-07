package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.convert;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseConvert {
    Class<?> value() default Object.class;
}
