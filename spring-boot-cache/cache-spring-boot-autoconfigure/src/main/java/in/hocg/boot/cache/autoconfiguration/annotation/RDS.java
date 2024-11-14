package in.hocg.boot.cache.autoconfiguration.annotation;

import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RDS {
    String value() default RedissonProperties.MASTER_FLAG;
}
