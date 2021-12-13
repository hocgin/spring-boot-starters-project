package in.hocg.boot.http.log;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpUtil;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLogBervice;
import in.hocg.boot.http.log.sample.BootApplication;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpLogTests extends AbstractSpringBootTest {
    @Autowired
    HttpLogBervice httpLogBervice;

    @Test
    public void test() {
        String urlString = "http://www.baidu.com/w?w=sds";
        String result = httpLogBervice.syncCall(() -> HttpUtil.get(urlString), () -> httpLogBervice.syncReady(null, null, null, null, null, null, null, null, urlString, null, null));
        Assert.notEmpty(result);
    }
}
