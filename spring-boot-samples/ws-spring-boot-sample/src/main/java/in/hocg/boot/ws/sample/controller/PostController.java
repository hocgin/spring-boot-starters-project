package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.web.result.Result;
import in.hocg.boot.ws.sample.cmd.TestCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class PostController {
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/send")
    public Result<Void> send() {
        messagingTemplate.convertAndSend("/message", new TestCmd().setOk("ok").setTest("测试"));
        return Result.success();
    }
}
