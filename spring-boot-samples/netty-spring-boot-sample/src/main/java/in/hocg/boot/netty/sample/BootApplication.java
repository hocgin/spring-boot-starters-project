package in.hocg.boot.netty.sample;

import in.hocg.boot.netty.sample.beinvoker.TestBeInvoker;
import in.hocg.boot.netty.sample.constant.NettyConstant;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.netty.core.invoker.BeInvokerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class BootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SpringContext.getBean(TestBeInvoker.class).testCommand("hi");
        BeInvokerManager.getThrowInvoker(NettyConstant.CMD_TEST1).invoke("hi2");
    }
}
