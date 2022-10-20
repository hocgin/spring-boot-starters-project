package in.hocg.boot.netty.sample.invoker;

import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.boot.netty.sample.pojo.PacketVo;
import in.hocg.netty.core.annotation.ChannelId;
import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.annotation.Invoker;
import in.hocg.netty.core.annotation.PacketData;

import java.io.Serializable;

@Invoker
public interface TestInvoker {

    @Command(66)
    PacketVo ping(@ChannelId Serializable id, @PacketData PacketRo ro);

}
