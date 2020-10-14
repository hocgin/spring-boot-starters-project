package in.hocg.boot.validation.autoconfigure.core.annotation;

import in.hocg.boot.validation.autoconfigure.core.StringRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hocgin on 2020/2/16.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = StringRangeValidator.class)
@Documented
public @interface StringRange {

    String message() default "参数错误";

    String[] strings() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
