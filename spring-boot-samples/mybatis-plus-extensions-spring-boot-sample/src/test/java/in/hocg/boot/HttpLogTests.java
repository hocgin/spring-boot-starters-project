package in.hocg.boot;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import in.hocg.boot.mybatis.plus.extensions.httplog.support.HttpLogUtils;
import in.hocg.boot.mybatis.plus.extensions.sample.BootApplication;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.boot.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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

    @Test
    public void test1() {
        String urlString = "http://www.baidu.com/w?w=sds";
        HttpRequest request = HttpUtil.createRequest(Method.POST, urlString);
        request.body("{\"name\":\"hocgin\"}");
        HttpResponse result = LogUtils.logSync(request::execute, HttpLogUtils.getReady(HttpLogUtils.request(request)),
            HttpLogUtils.getDefaultComplete());
        log.info("result: {}", result.body());
    }

    @Test
    public void test2() {
        String urlString = "http://www.baidu.com/w?w=sds";
        HttpRequest request = HttpUtil.createRequest(Method.POST, urlString);
        request.body("{\"name\":\"hocgin\"}");
        HttpResponse result = LogUtils.logAsync(request::execute, HttpLogUtils.getAsyncReady(HttpLogUtils.request(request)),
            HttpLogUtils.getDefaultComplete());
        log.info("result: {}", result.body());
    }
}
