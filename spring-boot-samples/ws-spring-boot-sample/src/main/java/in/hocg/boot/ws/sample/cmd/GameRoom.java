package in.hocg.boot.ws.sample.cmd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/6/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class GameRoom extends Room {
    private String id;
    private String passcode;
    private String master;
    private String mirror;

    @ApiModelProperty("待机玩家")
    private String waiter;
    @ApiModelProperty("待机玩家是否转为主机")
    @JsonProperty("asmaster")
    private Boolean asMaster = false;

    public GameRoom asGameRoom(String username) {
        GameRoom result = new GameRoom();
        result.setMaster(asMaster ? waiter : username)
            .setMirror(asMaster ? username : waiter)
            .setId(id).setGame(getGame());
        return result;
    }
}
