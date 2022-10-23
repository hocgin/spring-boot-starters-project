package in.hocg.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class ServerChannelClosedHandler extends ChannelInboundHandlerAdapter {
    private final NettyClient client;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("服务器掉线了, 正在尝试重连..");
        client.unbindChannel();
        client.reconnect();
    }
}
