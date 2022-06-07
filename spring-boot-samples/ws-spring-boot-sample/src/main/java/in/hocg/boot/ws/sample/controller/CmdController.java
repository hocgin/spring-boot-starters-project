package in.hocg.boot.ws.sample.controller;

import in.hocg.boot.utils.exception.ServiceException;
import in.hocg.boot.utils.struct.result.Result;
import in.hocg.boot.ws.autoconfiguration.core.WebSocketHelper;
import in.hocg.boot.ws.sample.cmd.TestCmd;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Controller
@MessageMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CmdController {
    private final SimpMessagingTemplate messagingTemplate;

    @ApiOperation("异常测试")
    @MessageMapping("/throw")
    public String thr(Principal principal) {
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

    @ApiOperation("回执给发送人")
    @MessageMapping("/toUser")
    @SendToUser(WebSocketHelper.PREFIX_USER + "/toUser")
    public String toUser() {
        return "hi";
    }

    @ApiOperation("消息路径变量")
    @MessageMapping("/path/{id}")
    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/path/{id}")
    public String pathVar(@DestinationVariable String id) {
        log.debug("--> {}", id);
        return id;
    }

    @ApiOperation("接收消息")
    @MessageMapping("/get")
    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/get/result")
    public Object get(@Header("X-Username") String username, @Payload Object body) {
        log.debug("--> {} {}", username, body);
        if (body instanceof byte[]) {
            return new String((byte[]) body);
        }
        return body;
    }

    @ApiOperation("对象接收")
    @MessageMapping("/obj")
    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/obj/result")
    public Result<String> roVo(@RequestBody TestCmd body) {
        log.debug("--> {}", body);
        return Result.success("ok");
    }
}
