package in.hocg.boot.web.autoconfiguration.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreTenant {

    /**
     * true为不做租户隔离 false为做租户隔离
     *
     * @return
     */
    boolean ignore() default true;
}
