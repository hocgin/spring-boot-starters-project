package in.hocg.boot.test.autoconfiguration;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import in.hocg.boot.mybatis.plus.autoconfiguration.MyBatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2022/3/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@MybatisPlusTest
@ImportAutoConfiguration(classes = MyBatisPlusAutoConfiguration.class)
public @interface MybatisTest {
    String[] properties() default {};

    boolean useDefaultFilters() default true;

    ComponentScan.Filter[] includeFilters() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    @AliasFor(
        annotation = ImportAutoConfiguration.class,
        attribute = "exclude"
    )
    Class<?>[] excludeAutoConfiguration() default {};
}
