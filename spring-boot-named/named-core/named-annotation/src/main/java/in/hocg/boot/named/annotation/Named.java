package in.hocg.boot.named.annotation;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2020/2/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Named {

    /**
     * 字典项标识
     *
     * @return id field
     */
    String idFor() default "";

    /**
     * 注入类型
     *
     * @return type
     */
    String type() default "";

    /**
     * 参数(默认为: {named.idFor()})
     *
     * @return []
     */
    String[] args() default {};

    /**
     * 是否使用缓存
     *
     * @return true
     */
    boolean useCache() default true;

    /**
     * 使用的 NamedService
     *
     * @return NamedService
     */
    Class<?> useService() default NamedService.class;

}
