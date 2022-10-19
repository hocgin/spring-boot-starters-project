package in.hocg.netty.core.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检查
 * - 15s 未读到数据则关闭连接
 *
 * @author hocgin
 */
@Slf4j
public class IdleStateCheck extends IdleStateHandler {
    private static final int READER_IDLE_TIME = 15;

    public IdleStateCheck() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.debug("{}秒内未读到数据，关闭连接", READER_IDLE_TIME);
        ctx.channel().close();
    }
}
