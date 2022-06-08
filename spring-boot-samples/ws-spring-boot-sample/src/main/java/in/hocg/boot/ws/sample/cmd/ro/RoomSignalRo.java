package in.hocg.boot.ws.sample.cmd.ro;

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
public class RoomSignalRo {
    private String id;
    private String signal;
}
