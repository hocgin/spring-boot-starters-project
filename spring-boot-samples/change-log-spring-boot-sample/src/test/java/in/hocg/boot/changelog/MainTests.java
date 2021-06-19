package in.hocg.boot.changelog;

import in.hocg.boot.changelog.autoconfiguration.compare.EntityCompare;
import in.hocg.boot.changelog.autoconfiguration.compare.FieldChangeDto;
import in.hocg.boot.changelog.autoconfiguration.core.ChangeLogBervice;
import in.hocg.boot.changelog.sample.BootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by hocgin on 2021/6/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class MainTests {
    @Autowired
    ChangeLogBervice changeLogService;

    @Test
    public void test() {
        List<FieldChangeDto> changes = EntityCompare.diffNotNull(this.createA(), this.createB());
        changeLogService.updateLog("test", 1L, changes, 1L);
    }

    private TestBean createA() {
        return new TestBean()
            .setCode("666");
    }

    private TestBean createB() {
        return createA()
            .setCode("777");
    }
}
