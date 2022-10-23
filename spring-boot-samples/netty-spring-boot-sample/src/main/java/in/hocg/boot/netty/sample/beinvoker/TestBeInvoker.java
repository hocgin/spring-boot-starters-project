package in.hocg.boot.netty.sample.beinvoker;

import in.hocg.boot.netty.sample.constant.NettyConstant;
import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.protocol.packet.Packet;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hocgin
 */
@Slf4j
@Service
public class TestBeInvoker {

    @ApiOperation("测试消息接收")
    @Command(NettyConstant.CMD_TEST1)
    public void testCommand(String source) {
        log.debug("CMD_TEST1.接收到的信息 {}", source);
    }

    @ApiOperation("测试 CMD_PING 指令接收")
    @Command(NettyConstant.CMD_PING)
    public void cmd2(Packet packet) {
        log.info("CMD_PING.接收到的信息 {}", packet.getData(PacketRo.class));
    }
}
