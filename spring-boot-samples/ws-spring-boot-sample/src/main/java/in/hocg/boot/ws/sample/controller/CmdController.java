package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.utils.exception.ServiceException;
import in.hocg.boot.ws.autoconfiguration.core.WebSocketHelper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CmdController {
    private final SimpMessagingTemplate messagingTemplate;


    @ApiOperation("异常测试")
    @MessageMapping("/index")
    @SendToUser(destinations = WebSocketHelper.PREFIX_USER + "/errors")
    public String index(Principal principal) {
        log.debug("--> {}", principal.getName());
        throw ServiceException.wrap("测试异常");
    }

    @ApiOperation("广播指令")
    @MessageMapping("/all")
    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/all")
    public String all(Principal principal) {
        log.debug("--> {}", principal);
        return "666 " + System.currentTimeMillis();
    }
}
