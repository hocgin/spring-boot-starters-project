package in.hocg.boot.kafka.sample;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.kafka.sample.producer.MessageProducer;
import in.hocg.boot.utils.struct.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@EnableKafka
@RestController
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class BootApplication {
    private final MessageProducer producer;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @GetMapping("/send")
    public Result<Void> send() {
        producer.sendMessage(StrUtil.format("当前时间: {}", DateUtil.now()));
        return Result.success();
    }
}
