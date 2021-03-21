package in.hocg.named.sample;

import cn.hutool.core.date.StopWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hocgin on 2020/8/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RestController
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SampleApplication {
    private final TestService service;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @GetMapping("/worked")
    public List<TestBean> worked() {


        // 10 * 100
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<TestBean> result = service.worked();
        stopWatch.stop();
        log.info("响应时间: {} s", stopWatch.getTotalTimeSeconds());
        return result;
    }
}
