package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.utils.struct.result.Result;
import in.hocg.boot.ws.autoconfiguration.core.WebSocketHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by hocgin on 2022/6/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Controller
@MessageMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class GameController {

    @MessageMapping("/room/{id}")
    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/room/{id}/result")
    public Result<Void> room() {
        return Result.success();
    }

}
