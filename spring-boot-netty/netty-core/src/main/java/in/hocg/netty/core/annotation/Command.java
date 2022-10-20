package in.hocg.netty.core.annotation;

import in.hocg.netty.core.constant.SystemPacketConstant;

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

    byte value();

    byte version() default SystemPacketConstant.Version;

    byte module() default SystemPacketConstant.DefaultModule;
}
