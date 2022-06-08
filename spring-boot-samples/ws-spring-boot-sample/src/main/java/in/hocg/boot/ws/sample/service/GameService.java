package in.hocg.boot.ws.sample.service;

import in.hocg.boot.ws.sample.cmd.ro.GameCmdRo;
import in.hocg.boot.ws.sample.cmd.ro.RoomSignalRo;

/**
 * Created by hocgin on 2022/6/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface GameService {
    void handleRoomRequest(GameCmdRo ro);

    void handleRoomSignal(RoomSignalRo ro, String username);
}
