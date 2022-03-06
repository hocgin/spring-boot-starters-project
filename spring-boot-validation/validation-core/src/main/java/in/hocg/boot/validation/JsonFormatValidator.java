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
    private Json.Type type;

    @Override
    public void initialize(Json constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(str) || Json.Type.Any.equals(type)) {
            return true;
        }

        if (Json.Type.Object.equals(type)) {
            return JSONUtil.isJsonObj(str);
        }

        if (Json.Type.Array.equals(type)) {
            return JSONUtil.isJsonArray(str);
        }

        return false;
    }
}
