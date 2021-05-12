package in.hocg.boot.web.jackson.bigdecimal;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@JsonSerialize(using = BigDecimalSerializer.class)
@JsonDeserialize(using = BigDecimalDeserializer.class)
public @interface BigDecimalFormat {
    /**
     * 格式化方式
     *
     * @return 格式化字符串
     */
    String value() default BigDecimalSerializer.DEFAULT_FORMAT;
}
