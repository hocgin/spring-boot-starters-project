package in.hocg.named.sample;

import in.hocg.named.sample.test.TestBean;
import in.hocg.named.sample.test.TestService;
import lombok.RequiredArgsConstructor;
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
        return service.worked();
    }
}
