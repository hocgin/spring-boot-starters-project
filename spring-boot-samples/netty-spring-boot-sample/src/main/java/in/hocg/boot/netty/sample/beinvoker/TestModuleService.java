package in.hocg.boot.netty.sample.beinvoker;

import in.hocg.boot.netty.sample.TestModule;
import in.hocg.netty.core.annotation.Command;
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
        log.debug("接收到的信息 {}", source);

//        ========================================================
//        todo: 从 redis 检索出发送目标的 topic 添加topic
//        ========================================================
    }
}
