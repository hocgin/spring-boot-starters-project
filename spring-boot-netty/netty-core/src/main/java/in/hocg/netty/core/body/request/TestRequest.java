package in.hocg.netty.core.body.request;

import in.hocg.netty.core.body.constant.TestModule;
import in.hocg.netty.core.protocol.AbstractPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 * <p>
 * 测试消息
 *
 * @author hocgin
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class TestRequest extends AbstractPacket {

    private String message;

    @Override
    public byte getCommand() {
        return TestModule.TEST_REQUEST;
    }

    @Override
    public byte getModule() {
        return TestModule.MODULE_VALUE;
    }
}
