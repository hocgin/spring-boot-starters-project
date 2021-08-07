package in.hocg.boot.named;

import com.google.common.base.Stopwatch;
import in.hocg.boot.named.annotation.Named;
import in.hocg.boot.named.autoconfiguration.aspect.NamedAspect;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.named.sample.BootApplication;
import in.hocg.named.sample.TestBean;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by hocgin on 2021/6/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Tests extends AbstractSpringBootTest {

    @Autowired
    NamedAspect namedAspect;

    @Test
    public void test() {
        ArrayList<TestBean> result = Lists.newArrayList(new TestBean());
        Stopwatch started = Stopwatch.createStarted();
        Object handleResult = namedAspect.handleResult(result);
        System.out.println(started.stop());
    }

    @Test
    public void test2() {
        Reflections reflections = new Reflections(".*", new SubTypesScanner(),
            new TypeAnnotationsScanner(), new FieldAnnotationsScanner());
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Named.class);
        System.out.println(fieldsAnnotatedWith);
    }
}
