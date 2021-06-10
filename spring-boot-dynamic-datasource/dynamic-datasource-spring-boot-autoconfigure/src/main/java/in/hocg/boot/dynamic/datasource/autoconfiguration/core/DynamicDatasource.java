package in.hocg.boot.dynamic.datasource.autoconfiguration.core;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDatasource {
    String name() default Constants.MAIN;
}
