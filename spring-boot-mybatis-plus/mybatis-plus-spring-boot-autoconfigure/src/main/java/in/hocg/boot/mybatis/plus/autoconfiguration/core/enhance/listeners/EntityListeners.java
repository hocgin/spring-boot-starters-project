package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2022/1/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityListeners {
    Class<? extends EntityListener> value() default EntityListener.class;
}
