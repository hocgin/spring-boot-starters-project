package in.hocg.boot.ws.sample.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.ws.autoconfiguration.core.event.SocketClosedEvent;
import in.hocg.boot.ws.sample.cmd.MessageCmdDto;
import in.hocg.boot.ws.sample.cmd.ro.GameCmdRo;
import in.hocg.boot.ws.sample.cmd.ro.RoomSignalRo;
import in.hocg.boot.ws.sample.cmd.GameRoom;
import in.hocg.boot.ws.sample.cmd.vo.GameCmdVo;
import in.hocg.boot.ws.sample.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2022/6/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class GameServiceImpl implements GameService {
    private final static Map<String, GameRoom> openRooms = Maps.newHashMap();
    private final static Map<String, GameRoom> reconnectRooms = Maps.newHashMap();
    private final static Map<String, GameRoom> connectedRooms = Maps.newHashMap();
    private final SimpMessagingTemplate messagingTemplate;

    private GameRoom create(String gameName, String username, Boolean anyone) {
        String passcode = RandomUtil.randomString(4);

        GameRoom room = new GameRoom();
        room.setMaster(username)
            .setType(GameRoom.Type.Open)
            .setGame(gameName);

        // 如果不需要公开
        if (!anyone) {
            room.setPasscode(passcode);
        }
        openRooms.put(passcode, room);
        return room;
    }

    private Optional<GameRoom> join(String passcode, String username) {
        GameRoom room = openRooms.get(passcode);
        if (Objects.isNull(room)) {
            return Optional.empty();
        } else {
            room = openRooms.remove(passcode);
            return startSession(room.getGame(), room.getMaster(), room.getMirror(), room.getId());
        }
    }

    private GameRoom anyone(String gameName, String username) {
        Optional<Map.Entry<String, GameRoom>> roomOpt = openRooms.entrySet().stream()
            .filter(entry -> {
                GameRoom room = entry.getValue();
                return StrUtil.isBlank(room.getPasscode()) && StrUtil.equals(room.getGame(), gameName);
            }).findFirst();
        if (roomOpt.isPresent()) {
            Map.Entry<String, GameRoom> room = roomOpt.get();
            return join(room.getKey(), username).orElseThrow();
        } else {
            return create(gameName, username, true);
        }
    }

    private Optional<GameRoom> reconnect(String roomId, String gameName, String username, Boolean asMaster) {
        if (reconnectRooms.containsKey(roomId)) {
            GameRoom room = reconnectRooms.remove(roomId).asGameRoom(username);
            return startSession(room.getGame(), room.getMaster(), room.getMirror(), room.getId());
        } else {
            GameRoom room = new GameRoom();
            room.setWaiter(username)
                .setId(roomId)
                .setAsMaster(asMaster)
                .setGame(gameName);
            return Optional.ofNullable(reconnectRooms.put(roomId, room));
        }
    }

    /**
     * 放弃联机房间
     *
     * @param roomId
     * @param gameName
     * @param username
     * @return
     */
    private Optional<GameRoom> abandon(String roomId, String gameName, String username) {
        if (StrUtil.isNotBlank(roomId)) {
            reconnectRooms.remove(roomId);
            return Optional.ofNullable(connectedRooms.remove(roomId));
        } else {
            openRooms.entrySet().stream()
                .filter(entry -> {
                    GameRoom room = entry.getValue();
                    return StrUtil.equals(room.getGame(), gameName) && StrUtil.equals(room.getMaster(), username);
                }).map(Map.Entry::getKey).forEach(openRooms::remove);
        }
        return Optional.empty();
    }

    /**
     * 关闭连接
     */
    @EventListener(classes = SocketClosedEvent.class)
    public void close(SocketClosedEvent event) {
        Principal principal = event.getSession().getPrincipal();
        if (Objects.isNull(principal)) {
            return;
        }
        String username = principal.getName();

        openRooms.entrySet().stream()
            .filter(entry -> StrUtil.equals(entry.getValue().getMaster(), username))
            .map(Map.Entry::getKey)
            .forEach(openRooms::remove);

        reconnectRooms.values().stream()
            .filter(room -> StrUtil.equals(room.getWaiter(), username))
            .map(GameRoom::getId)
            .forEach(reconnectRooms::remove);

        connectedRooms.values().stream()
            .filter(room -> StrUtil.equals(room.getMaster(), username) || StrUtil.equals(room.getMirror(), username))
            .map(GameRoom::getId)
            .forEach(connectedRooms::remove);
    }

    private Optional<GameRoom> startSession(String gameName, String master, String mirror, String id) {
        String roomId = LangUtils.getOrDefault(id, IdUtil.randomUUID());
        if (connectedRooms.containsKey(roomId)) {
            return Optional.empty();
        }
        GameRoom room = new GameRoom();
        room.setMaster(master)
            .setId(roomId)
            .setMirror(mirror)
            .setType(GameRoom.Type.Connected)
            .setGame(gameName);
        return Optional.ofNullable(connectedRooms.put(roomId, room));
    }

    private Optional<GameRoom> getConnectedRoom(String roomId) {
        return Optional.ofNullable(connectedRooms.get(roomId));
    }

    @Override
    public void handleRoomRequest(GameCmdRo ro) {
        // 当前操作人
        String username = ro.getUsername();

        GameRoom result = null;
        switch (ro.getType()) {
            // 在线匹配
            case "anyone": {
                Assert.notEmpty(ro.getGame(), "game is empty");
                Assert.notEmpty(ro.getUsername(), "username is empty");
                result = anyone(ro.getGame(), ro.getUsername());
                break;
            }
            // 创建房间
            case "create": {
                Assert.notEmpty(ro.getGame(), "game is empty");
                Assert.notEmpty(ro.getUsername(), "username is empty");
                result = create(ro.getGame(), ro.getUsername(), false);
                break;
            }
            // 加入房间
            case "join": {
                Assert.notEmpty(ro.getGame(), "game is empty");
                Assert.notEmpty(ro.getUsername(), "username is empty");
                result = join(ro.getPasscode(), ro.getUsername()).orElse(null);
                break;
            }
            // 重连
            case "masterReconnect":
            case "mirrorReconnect": {
                Assert.notEmpty(ro.getId(), "id is empty");
                Assert.notEmpty(ro.getGame(), "game is empty");
                Assert.notEmpty(ro.getUsername(), "username is empty");
                result = reconnect(ro.getId(), ro.getGame(), ro.getUsername(), "masterReconnect".equals(ro.getType())).orElse(null);
                break;
            }
            // 离开房间
            case "abandon": {
                abandon(ro.getId(), ro.getGame(), ro.getUsername())
                    // 有人离开房间了，通知另外一个玩家
                    .ifPresent(abandonRoom -> {
                        String sender = ro.getUsername();
                        String receiver = StrUtil.equals(abandonRoom.getMaster(), sender) ? abandonRoom.getMirror() : abandonRoom.getMaster();

                        if (StrUtil.isNotBlank(receiver)) {
                            sendRoomRequestToUser(receiver, new GameCmdVo()
                                .setStatus("abandon")
                                .setId(abandonRoom.getId())
                                .setGame(abandonRoom.getGame())
                                .setThat(receiver)
                                .setThat(sender));
                        }
                    });
                break;
            }
            // 打印日志
            default:
                log.warn("unknown room request type: {}", ro.getType());
        }

        if (Objects.isNull(result)) {
            return;
        }

        switch (result.getType()) {
            case Open: {
                sendRoomRequestToUser(username, new GameCmdVo()
                    .setStatus("created")
                    .setGame(result.getGame())
                    .setPasscode(result.getPasscode())
                    .setThiz(result.getMaster()));
                break;
            }
            case Reconnect: {
                sendRoomRequestToUser(username, new GameCmdVo()
                    .setGame(result.getGame())
                    .setThiz(result.getWaiter())
                    .setId(result.getId())
                    .setStatus("reconnect"));
                break;
            }
            case Connected: {
                sendRoomRequestToUser(username, new GameCmdVo()
                    .setStatus("masterJoined")
                    .setGame(result.getGame())
                    .setThat(result.getMirror())
                    .setThiz(result.getMaster())
                    .setId(result.getId()));

                sendRoomRequestToUser(username, new GameCmdVo()
                    .setStatus("mirrorJoined")
                    .setGame(result.getGame())
                    .setThiz(result.getMirror())
                    .setThat(result.getMaster())
                    .setId(result.getId()));
                break;
            }
            default: {
                sendRoomRequestToUser(username, new GameCmdVo()
                    .setStatus("join".equals(ro.getType()) ? "passcodeInvalid" : "roomIdInvalid")
                    .setGame(result.getGame())
                    .setThiz(username));
            }
        }
    }

    @Override
    public void handleRoomSignal(RoomSignalRo ro, String username) {
        String roomId = ro.getId();
        String signal = ro.getSignal();
        Assert.notEmpty(roomId, "id is empty");
        Assert.notEmpty(signal, "signal is empty");
        getConnectedRoom(roomId).ifPresent(room -> {
            String mirror = room.getMirror();
            String master = room.getMaster();
            if (StrUtil.equals(master, username) && StrUtil.isNotBlank(mirror)) {
                sendRoomSignalRequestToUser(mirror, signal);
            } else if (StrUtil.equals(mirror, username) && StrUtil.isNotBlank(master)) {
                sendRoomSignalRequestToUser(master, signal);
            }
        });
    }

    private void sendRoomRequestToUser(String username, GameCmdVo vo) {
        messagingTemplate.convertAndSendToUser(username, "", new MessageCmdDto()
            .setName("room")
            .setValue(vo));
    }

    private void sendRoomSignalRequestToUser(String username, String signal) {
        messagingTemplate.convertAndSendToUser(username, "", new MessageCmdDto()
            .setName("room.signal")
            .setValue(signal));
    }
}
