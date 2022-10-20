package in.hocg.netty.server.netty.initializer;

import in.hocg.netty.core.protocol.IdleStateCheck;
import in.hocg.netty.core.protocol.Splitter;
import in.hocg.netty.server.netty.handler.DispatcherHandler;
import in.hocg.netty.core.protocol.codec.MessageDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2019/3/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class SocketInitializer extends ChannelInitializer<Channel> {
    private final DispatcherHandler dispatcherHandler;

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
            .addLast(new LoggingHandler(LogLevel.DEBUG))
            .addLast("IDLE-STATE-CHECK", new IdleStateCheck())
            .addLast("SPLITTER", new Splitter())
            .addLast("MESSAGE-DECODE", new MessageDecoder())
            // 业务处理器
            .addLast("FORWARD-HANDLER", dispatcherHandler)
        ;
    }
}
