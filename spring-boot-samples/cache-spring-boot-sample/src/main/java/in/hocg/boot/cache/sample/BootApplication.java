package in.hocg.boot.cache.sample;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.cache.sample.cache.impl.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class BootApplication {
    private final CacheService cacheService;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Async
    @GetMapping("/")
    public void run() throws Exception {
        while (true) {
            cacheService.useCache(IdUtil.randomUUID());
            Thread.sleep(5 * 1000L);
        }
    }
}
