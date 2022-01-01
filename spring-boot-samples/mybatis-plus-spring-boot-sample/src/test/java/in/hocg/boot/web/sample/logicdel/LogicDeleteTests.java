package in.hocg.boot.web.sample.logicdel;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import in.hocg.boot.web.sample.BaseDbTest;
import in.hocg.boot.web.sample.TestApplication;
import in.hocg.boot.web.sample.data.basic.ModelEntity;
import in.hocg.boot.web.sample.data.basic.ModelEntityMapper;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by hocgin on 2021/12/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class LogicDeleteTests extends BaseDbTest<ModelEntityMapper> {

    @org.junit.Test
    public void testDelete() {
        doTestAutoCommit(mapper -> {
            int delete = mapper.deleteById(1);
            assertEquals(delete, 1);

            delete = mapper.delete(Wrappers.<ModelEntity>lambdaQuery().eq(ModelEntity::getId, 2));
            assertEquals(delete, 1);
        });

        doTest(i -> {
            ModelEntity entity = i.byId(1L);
            assertNotNull(entity);
//            assertNotNull(entity.getDeletedAt());

            entity = i.byId(2L);
            assertNotNull(entity);
//            assertNotNull(entity.getDeletedAt());
        });

        doTest(mapper -> {
            ModelEntity entity = new ModelEntity();
            mapper.insert(entity);

            // todo BUG
            assertEquals(mapper.deleteById(entity), 1);
        });
    }


    @Override
    protected String tableDataSql() {
        return FileUtil.readUtf8String("./logicdel/sql/data.sql");
    }

    @Override
    protected List<String> tableSql() {
        return Collections.singletonList(FileUtil.readUtf8String("./logicdel/sql/table.sql"));
    }
}
