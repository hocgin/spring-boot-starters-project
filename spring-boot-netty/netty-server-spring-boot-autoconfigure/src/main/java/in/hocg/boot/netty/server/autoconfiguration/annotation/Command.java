package in.hocg.boot.netty.server.autoconfiguration.annotation;

import in.hocg.boot.netty.server.autoconfiguration.bean.InvokerManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hocgin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();

    String module() default InvokerManager.DEFAULT_MODULE;
}
