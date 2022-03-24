package in.hocg.boot.mybatis.plus.sample.web.sample.fill;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.sample.web.sample.BaseDbTest;
import in.hocg.boot.mybatis.plus.sample.web.sample.TestApplication;
import in.hocg.boot.mybatis.plus.sample.web.sample.data.none.NoneModelEntity;
import in.hocg.boot.mybatis.plus.sample.web.sample.data.none.NoneModelEntityMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class NoneModelTests extends BaseDbTest<NoneModelEntityMapper> {

    @Test
    public void testFill() {
        doTestAutoCommit(mapper -> {
            NoneModelEntity update = new NoneModelEntity();
            update.setId(1L);
            update.setTitle("ut1");
            int i = mapper.updateById(update);
            Assert.assertEquals(i, 1);


            NoneModelEntity select = mapper.selectById(1L);
            Assert.assertEquals("ut1", select.getTitle());
        });

    }

    @Override
    protected String tableDataSql() {
        return FileUtil.readUtf8String("./fill/sql/data.sql");
    }

    @Override
    protected List<String> tableSql() {
        return Lists.newArrayList(FileUtil.readUtf8String("./fill/sql/table.sql"));
    }
}
