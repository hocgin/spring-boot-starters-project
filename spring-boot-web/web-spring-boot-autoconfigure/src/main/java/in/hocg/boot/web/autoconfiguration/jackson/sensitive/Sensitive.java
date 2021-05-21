package in.hocg.boot.web.autoconfiguration.jackson.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = SensitiveSerializer.class)
public @interface Sensitive {
    SensitiveType type() default SensitiveType.CUSTOMER;

    /**
     * 打码标记
     */
    String mask() default "*";

    /**
     * 前置不打码长度
     */
    int prefixNoMaskSize() default 0;

    /**
     * 后置不打码长度
     */
    int suffixNoMaskSize() default 0;
}
