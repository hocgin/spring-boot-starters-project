package in.hocg.boot.ws.sample.cmd.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/6/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class GameCmdVo implements Serializable {
    @ApiModelProperty(value = "状态: created/masterJoined/mirrorJoined/reconnect/roomIdInvalid/passcodeInvalid/abandoned",
        required = true)
    private String status;
    @ApiModelProperty(required = true)
    private String game;
    @JsonAlias("this")
    @ApiModelProperty(value = "玩家", required = true)
    private String thiz;
    @ApiModelProperty("对手")
    private String that;
    @ApiModelProperty("房间号")
    private String passcode;
    @ApiModelProperty("id")
    private String id;
}
