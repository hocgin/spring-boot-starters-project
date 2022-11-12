package in.hocg.boot.cache.autoconfiguration.annotation;

import in.hocg.boot.cache.autoconfiguration.enums.LockType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {

    /**
     * 锁的模式
     * - 如果不设置,自动模式，当参数只有一个，使用 REENTRANT
     * - 参数多个 MULTIPLE
     */
    LockType lockType() default LockType.Auto;

    /**
     * 锁的 Key
     * 如果 keys 有多个, 如果不设置锁类型, 则使用联锁
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 锁常量，通常用来辨别锁的。
     *
     * @return
     */
    String keyConstant() default "";

    /**
     * 锁的时间长度。
     * 0: 表示读取全局配置
     *
     * @return
     */
    long lockTimeout() default 0;

    /**
     * 加锁失败，再尝试加锁的超时时间。
     * -1: 则表示一直等待
     * 0: 表示读取全局配置
     *
     * @return
     */
    long tryWaitTimeout() default 0;
}
