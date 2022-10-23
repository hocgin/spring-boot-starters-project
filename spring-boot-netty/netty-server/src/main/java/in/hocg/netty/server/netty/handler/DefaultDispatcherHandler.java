package in.hocg.netty.server.netty.handler;

import in.hocg.netty.core.invoker.BeInvokerManager;
import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hocgin
 */
@Slf4j
public class DefaultDispatcherHandler extends DispatcherHandler {
    @Override
    public void channelRead0(ChannelHandlerContext context, Packet packet) {
        String fromChannelId = context.channel().id().asLongText();
        log.debug("DefaultDispatcher.转发消息.channelId=[{}].[packet={}]", fromChannelId, packet);
        BeInvokerManager.getInvoker(packet.getModule(), packet.getCommand()).ifPresent(method -> method.invoke(packet));
    }
}
