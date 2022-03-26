package in.hocg.boot.mybatis.plus.sample.service.impl;

import in.hocg.boot.mybatis.plus.sample.mapper.ExampleMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExampleServiceImplTest {
    @Autowired(required = false)
    private ExampleMapper service;

    @Test
    public void testAs() {
        String index = service.index();
        System.out.println("hi" + index);
    }
}
