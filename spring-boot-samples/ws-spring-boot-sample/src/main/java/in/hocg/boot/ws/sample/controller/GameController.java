package in.hocg.boot.ws.sample.controller;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.ws.sample.cmd.MessageCmdDto;
import in.hocg.boot.ws.sample.cmd.ro.GameCmdRo;
import in.hocg.boot.ws.sample.cmd.ro.RoomSignalRo;
import in.hocg.boot.ws.sample.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

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
    private final GameService service;

    @MessageMapping("/game")
//    @SendTo(WebSocketHelper.PREFIX_BROADCAST + "/room/{id}/rosult")
    public void room(@RequestBody MessageCmdDto dto, Principal principal) {
        switch (dto.getName()) {
            case "room": {
                service.handleRoomRequest(JSONUtil.toBean(JSONUtil.toJsonStr(dto.getValue()), GameCmdRo.class));
                return;
            }
            case "room.signal": {
                service.handleRoomSignal(JSONUtil.toBean(JSONUtil.toJsonStr(dto.getValue()), RoomSignalRo.class), principal.getName());
                return;
            }
            default: {
                log.warn("未知指令: {}", dto.getName());
            }
        }
    }

}
