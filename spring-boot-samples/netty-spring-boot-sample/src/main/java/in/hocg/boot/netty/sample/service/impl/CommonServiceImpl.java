package in.hocg.boot.netty.sample.service.impl;

import in.hocg.boot.netty.sample.invoker.TestInvoker;
import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.boot.netty.sample.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final TestInvoker invoker;

    @Override
    public void ping() {
        invoker.ping(100L, new PacketRo());
    }
}
