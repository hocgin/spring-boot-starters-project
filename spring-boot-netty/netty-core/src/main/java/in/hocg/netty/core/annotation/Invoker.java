package in.hocg.netty.core.annotation;

import in.hocg.netty.core.session.SessionManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Invoker {
    SessionManager.ChannelType value() default SessionManager.ChannelType.Auto;
}
