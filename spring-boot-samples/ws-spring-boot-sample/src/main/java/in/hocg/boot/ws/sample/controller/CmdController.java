package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.web.result.Result;
import in.hocg.boot.ws.sample.cmd.TestCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
//@MessageMapping("/cmd")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CmdController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/index")
    public Result<Void> index() {
        return Result.success();
    }

    @MessageMapping("/success")
    public Result<Void> success() {
        return Result.success();
    }

    @MessageMapping("/path/{id}")
    public Result<String> path(@DestinationVariable String id) {
        return Result.success(id);
    }

    @MessageMapping("/message")
    public Result<Object> message(TestCmd cmd) {
        return Result.success(cmd);
    }


}
