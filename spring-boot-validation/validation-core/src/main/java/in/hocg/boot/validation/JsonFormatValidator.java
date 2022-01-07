package in.hocg.boot.validation;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.validation.annotation.Json;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by hocgin on 2022/1/3
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class JsonFormatValidator implements ConstraintValidator<Json, String> {
    private boolean isObject = true;
    private boolean isArray = true;

    @Override
    public void initialize(Json constraintAnnotation) {
        isObject = constraintAnnotation.isObject();
        isArray = constraintAnnotation.isArray();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(str)) {
            return true;
        }

        if (isObject && isArray) {
            return JSONUtil.isJson(str);
        }

        if (isObject) {
            return JSONUtil.isJsonObj(str);
        }

        if (isArray) {
            return JSONUtil.isJsonArray(str);
        }

        return false;
    }
}
