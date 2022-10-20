package in.hocg.netty.server.netty.handler;

import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultForwardHandler extends AbsForwardHandler {
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        log.info("-----> DefaultForwardHandler.channelRead0.[packet={}]", packet);
    }
}
