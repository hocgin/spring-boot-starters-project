package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.utils.struct.result.Result;
import in.hocg.boot.ws.autoconfiguration.core.MessageHelper;
import in.hocg.boot.ws.sample.cmd.TestCmd;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final SimpUserRegistry simpUserRegistry;

    @ApiOperation("手动单播")
    @GetMapping("/{id}/send")
    public Result<Void> send(@PathVariable String id) {
        TestCmd payload = new TestCmd().setOk("ok").setTest("测试");
        messagingTemplate.convertAndSendToUser(id, MessageHelper.toUser("/errors"), payload);
        return Result.success();
    }

    @ApiOperation("手动广播")
    @GetMapping("/send")
    public Result<Void> sendAll() {
        TestCmd payload = new TestCmd().setOk("ok").setTest("测试");
        messagingTemplate.convertAndSend(MessageHelper.toBroadcast("/all"), payload);
        return Result.success();
    }
}
