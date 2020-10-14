package in.hocg.boot.validation.autoconfigure.core;


import in.hocg.boot.validation.autoconfigure.core.annotation.EnumRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by hocgin on 2020/2/16.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class EnumRangeValidator implements ConstraintValidator<EnumRange, Serializable> {
    private Class<? extends Enum>[] enumClass;

    @Override
    public void initialize(EnumRange constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Serializable code, ConstraintValidatorContext context) {
        if (Objects.isNull(code)) {
            return true;
        }

        ICode intEnum;
        for (Class<? extends Enum> aClass : enumClass) {
            if (ICode.class.isAssignableFrom(aClass)) {
                for (Enum enumConstant : aClass.getEnumConstants()) {
                    intEnum = (ICode) enumConstant;
                    if (intEnum.eq(code)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
