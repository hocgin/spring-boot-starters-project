package in.hocg.boot.distributed.lock.annotation;


import in.hocg.boot.distributed.lock.core.KeyType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2019-09-29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface UseLock {

    /**
     * 锁前缀
     *
     * @return
     */
    String prefix() default "LOCK#";

    /**
     * 指定锁
     *
     * @return
     */
    String key() default "";

    /**
     * 锁的类型
     *
     * @return
     */
    KeyType keyType() default KeyType.Key;

    /**
     * 获取不到锁的错误信息
     *
     * @return
     */
    String errorMessage() default "系统繁忙，请稍后";

    /**
     * 尝试次数
     *
     * @return
     */
    int tryNumber() default 1;

    /**
     * 尝试间隔
     *
     * @return
     */
    long tryInterval() default 0;

    /**
     * 超时时间
     *
     * @return
     */
    long expireTime() default 30;

    /**
     * 超时时间单位
     *
     * @return
     */
    TimeUnit expireTimeUnit() default TimeUnit.SECONDS;
}
