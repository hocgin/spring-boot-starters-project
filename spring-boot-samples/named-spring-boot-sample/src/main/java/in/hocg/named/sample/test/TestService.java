package in.hocg.named.sample.test;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
public class TestService {

    public List<TestBean> worked() {
        List<TestBean> result = Lists.newArrayList();

        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        result.add(new TestBean().setCode(IdUtil.fastUUID()));
        return result;
    }
}
