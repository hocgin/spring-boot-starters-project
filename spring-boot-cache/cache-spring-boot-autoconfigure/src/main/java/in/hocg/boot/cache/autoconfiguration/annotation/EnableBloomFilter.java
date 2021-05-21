package in.hocg.boot.cache.autoconfiguration.annotation;

import in.hocg.boot.cache.autoconfiguration.BloomFilterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BloomFilterAutoConfiguration.class)
@Documented
public @interface EnableBloomFilter {
}
