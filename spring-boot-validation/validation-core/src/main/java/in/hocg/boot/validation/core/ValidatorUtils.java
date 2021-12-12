package in.hocg.boot.validation.core;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by hocgin on 2020/3/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ValidatorUtils {

    public static ValidatorFactory getValidatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }

    public static Validator getValidation() {
        return getValidatorFactory().getValidator();
    }

    public static <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return getValidation().validate(object, groups);
    }

    public static <T> T validThrow(T object, Class<?>... groups) {
        final Set<ConstraintViolation<T>> validate = ValidatorUtils.validate(object, groups);
        for (ConstraintViolation<T> violation : validate) {
            throw new ValidationException(violation.getMessage());
        }
        return object;
    }
}
