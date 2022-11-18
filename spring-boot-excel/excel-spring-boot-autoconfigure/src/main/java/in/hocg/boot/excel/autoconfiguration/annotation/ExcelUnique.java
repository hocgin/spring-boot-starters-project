package in.hocg.boot.excel.autoconfiguration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelUnique {
    /**
     * 错误信息
     */
    String message() default "表格内出现重复数据";
}
