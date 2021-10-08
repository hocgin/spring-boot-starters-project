package in.hocg.boot.named;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.base.Stopwatch;
import in.hocg.boot.named.annotation.Named;
import in.hocg.boot.named.autoconfiguration.utils.NamedUtils;
import in.hocg.named.sample.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Created by hocgin on 2021/6/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class JTests {

    /**
     * 209.9 ms 160.6 ms 180.4 ms
     * 297.7 μs 316.7 μs 234.6 μs
     */
    @Test
    public void testHutool() {
        Stopwatch started = Stopwatch.createStarted();
        NamedUtils.getAllField(TestBean.class);
        started = started.stop();
        System.out.println(started);
        started.reset().start();
        NamedUtils.getAllField(TestBean.class);
        System.out.println(started.stop());
    }

    /**
     * 13.92 ms 12.12 ms 12.58 ms
     * 28.54 μs 27.30 μs 27.02 μs
     */
    @Test
    public void testHutool2() {
        Stopwatch started = Stopwatch.createStarted();
        ReflectUtil.getFieldMap(TestBean.class);
        started = started.stop();
        System.out.println(started);
        started.reset().start();
        Map<String, Field> fieldMap = ReflectUtil.getFieldMap(TestBean.class);
        System.out.println(started.stop());
    }

    @Test
    public void testReflections() {
//        ConfigurationBuilder.build("").set
        Reflections reflections = new Reflections(TestBean.class, new SubTypesScanner(),
            new TypeAnnotationsScanner(), new FieldAnnotationsScanner());
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Named.class);
        System.out.println(fieldsAnnotatedWith);
    }
}
