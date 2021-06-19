package in.hocg.mail.spring.boot.samples;

import in.hocg.boot.mail.autoconfigure.core.MailBervice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2019/6/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class IndexController {
    private final MailBervice service;

    @GetMapping("/send-mail")
    public String sendSms() {
        return service.sendText("hocgin@gmail.com", "测试内容", "你好");
    }
}
