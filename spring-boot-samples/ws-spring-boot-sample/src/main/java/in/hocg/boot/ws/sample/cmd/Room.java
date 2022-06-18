package in.hocg.boot.ws.sample.cmd;

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
public class Room {
    private Type type;
    private String game;

    public enum Type {
        Open,
        Connected,
        Reconnect,
    }
}
