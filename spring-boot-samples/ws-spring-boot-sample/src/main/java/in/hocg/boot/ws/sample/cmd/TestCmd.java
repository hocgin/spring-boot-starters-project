package in.hocg.boot.ws.sample.cmd;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class TestCmd {
    private String test;
    private String ok;
    private byte[] bytes;
}
