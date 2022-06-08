package in.hocg.boot.ws.sample.cmd;

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
public class MessageCmdDto implements Serializable {
    @ApiModelProperty(required = true, value = "类型: room/room.signal")
    private String name;
    private Object value;
}
