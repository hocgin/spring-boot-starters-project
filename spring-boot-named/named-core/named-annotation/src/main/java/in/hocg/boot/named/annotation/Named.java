package in.hocg.boot.named.annotation;

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
public @interface Named {

    /**
     * 字典项标识
     *
     * @return id field
     */
    String idFor();

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

}
