package in.hocg.boot.cache.autoconfiguration.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解限流器
 *
 * @author hocgin
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流唯一标示
     */
    String key() default "";

    /**
     * 限流时间 单位 s
     */
    long time() default 5;

    /**
     * 限流次数
     */
    long count() default 1;
}
