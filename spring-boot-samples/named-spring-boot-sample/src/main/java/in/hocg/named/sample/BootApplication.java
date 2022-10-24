package in.hocg.named.sample;

import cn.hutool.core.date.StopWatch;
import in.hocg.named.sample.pojo.TestBeanDto;
import in.hocg.named.sample.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

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
public class BootApplication {
    private final TestService service;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @GetMapping("/worked")
    public List<TestBeanDto> worked() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<TestBeanDto> result = service.worked();
        watch.stop();
        log.info("{}, 响应时间: {} s", Objects.nonNull(result.get(0).getCode2Name()), watch.getLastTaskInfo().getTimeSeconds());
        return result;
    }

//    @GetMapping("/ipage")
//    public IPage<TestBean> ipage() {
//        return service.ipage();
//    }
//
//    @GetMapping("/page")
//    public IPage<TestBean> page() {
//        return service.page();
//    }
}
