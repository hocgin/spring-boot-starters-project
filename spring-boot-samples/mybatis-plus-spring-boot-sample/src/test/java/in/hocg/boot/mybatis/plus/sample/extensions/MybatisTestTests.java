package in.hocg.boot.mybatis.plus.sample.extensions;

import in.hocg.boot.mybatis.plus.sample.mapper.ExampleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hocgin on 2022/3/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
class MybatisTestTests {
    @Autowired(required = false)
    private ExampleMapper service;

    @Test
    public void testInsert() {
        String index = service.index();
        System.out.println(index);
    }
}
