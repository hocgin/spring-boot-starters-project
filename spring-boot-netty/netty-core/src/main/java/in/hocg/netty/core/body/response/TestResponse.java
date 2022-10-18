package in.hocg.netty.core.body.response;

import in.hocg.netty.core.body.constant.TestModule;
import in.hocg.netty.core.protocol.AbstractPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 * <p>
 * 测试消息
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestResponse extends AbstractPacket {

    private String message;

    @Override
    public byte getCommand() {
        return TestModule.TEST_RESPONSE;
    }

    @Override
    public byte getModule() {
        return TestModule.MODULE_VALUE;
    }
}
