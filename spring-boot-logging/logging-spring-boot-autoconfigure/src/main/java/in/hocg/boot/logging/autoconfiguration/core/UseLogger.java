package in.hocg.boot.logging.autoconfiguration.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by hocgin on 2020/2/17.
 * email: hocgin@gmail.com
 * 在要进行函数执行报告的函数上进行使用
 *
 * @author hocgin
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface UseLogger {
    /**
     * 接口的功能描述
     *
     * @return
     */
    String value() default "未填写";
}
