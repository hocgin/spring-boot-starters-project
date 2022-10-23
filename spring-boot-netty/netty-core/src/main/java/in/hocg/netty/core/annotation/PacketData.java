package in.hocg.netty.core.annotation;

import in.hocg.netty.core.serializer.SerializerAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketData {
    SerializerAlgorithm algorithm() default SerializerAlgorithm.JSON;
}
