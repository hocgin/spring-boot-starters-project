package in.hocg.boot.validation.annotation;

import in.hocg.boot.validation.JsonFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hocgin on 2022/1/3
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = JsonFormatValidator.class)
@Documented
public @interface Json {

    String message() default "参数错误";

    boolean isObject() default true;

    boolean isArray() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
