package in.hocg.boot.web.autoconfiguration.aspect;

import java.lang.annotation.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface TimeoutCheck {

    /**
     * 超时时间，默认5秒
     */
    long timeout() default 5L;

    /**
     * 超时单位，默认秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 超时后是否销毁线程
     */
    boolean destroy() default true;

    /**
     * 指定线程池 Bean, 默认不使用
     * @see Executor
     */
    String threadPool() default "";
}
