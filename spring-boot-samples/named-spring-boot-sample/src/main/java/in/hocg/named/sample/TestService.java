package in.hocg.named.sample;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@Slf4j
public class TestService {

    public List<TestBean> worked() {
        StopWatch watch = new StopWatch();
        watch.start();

        List<TestBean> result = Lists.newArrayList();

        TestBean ele = new TestBean().setCode(IdUtil.fastUUID());
        ele.setList(Lists.newArrayList(new TestBean().setCode(IdUtil.fastUUID())));
        result.add(ele);
        result.add(new TestBean().setCode(10086));
        result.add(new TestBean().setCode(true));
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

        watch.stop();
        log.info("业务耗时: {} ns", watch.getTotalTimeNanos());
        return result;
    }

//    public IPage<TestBean> ipage() {
//        return this.page();
//    }
//
//    public Page<TestBean> page() {
//        return new Page<TestBean>().setRecords(this.worked());
//    }
}
