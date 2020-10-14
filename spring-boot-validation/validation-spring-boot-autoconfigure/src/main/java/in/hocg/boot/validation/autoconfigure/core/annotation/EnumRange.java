package in.hocg.boot.validation.autoconfigure.core.annotation;

import in.hocg.boot.validation.autoconfigure.core.EnumRangeValidator;

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
@Constraint(validatedBy = EnumRangeValidator.class)
@Documented
public @interface EnumRange {

    String message() default "参数错误";

    Class<? extends Enum>[] enumClass() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
