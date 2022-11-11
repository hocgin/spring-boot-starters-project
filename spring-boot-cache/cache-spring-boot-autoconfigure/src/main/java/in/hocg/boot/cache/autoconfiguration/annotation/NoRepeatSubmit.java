package in.hocg.boot.cache.autoconfiguration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防重复提交注解
 *
 * @author hocgin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 自定义key
     */
    String key() default "";

    /**
     * 默认时间5秒
     */
    long timeout() default 5;
}
