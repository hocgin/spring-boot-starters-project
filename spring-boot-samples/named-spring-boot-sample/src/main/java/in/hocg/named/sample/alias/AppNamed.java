package in.hocg.named.sample.alias;

import in.hocg.boot.named.annotation.Named;
import in.hocg.named.sample.basic.AppNamedService;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2021/12/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Named(useService = AppNamedService.class)
public @interface AppNamed {

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
}
