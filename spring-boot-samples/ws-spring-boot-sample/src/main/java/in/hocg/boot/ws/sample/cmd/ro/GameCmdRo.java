package in.hocg.boot.ws.sample.cmd.ro;

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
public class GameCmdRo {
    @ApiModelProperty(value = "指令类型: anyone/create/join/masterReconnect/mirrorReconnect/abandon", required = true)
    private String type;
    @ApiModelProperty(required = true)
    private String game;
    @ApiModelProperty(required = true)
    private String username;

    @ApiModelProperty
    private String id;
    @ApiModelProperty
    private String passcode;
}
