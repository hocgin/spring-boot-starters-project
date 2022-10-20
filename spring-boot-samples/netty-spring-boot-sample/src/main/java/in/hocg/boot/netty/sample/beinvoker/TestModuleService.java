package in.hocg.boot.netty.sample.beinvoker;

import in.hocg.boot.netty.sample.TestModule;
import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.protocol.packet.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hocgin
 */
@Slf4j
@Service
public class TestModuleService {

    @Command(TestModule.TEST_REQUEST)
    public void testCommand(String source) {
        log.debug("testCommand.接收到的信息 {}", source);
    }

    @Command(TestModule.TEST_REQUEST_2)
    public void test2Command(Packet packet) {
        log.info("test2Command.接收到的信息 {}", packet);
        log.info("test2Command.packet.接收到的信息 {}", packet.getData(PacketRo.class));
    }
}
