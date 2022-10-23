package in.hocg.boot.netty.server.autoconfiguration.core;

import in.hocg.boot.message.autoconfigure.MessageFactory;
import in.hocg.netty.core.constant.MessageConstant;
import in.hocg.netty.core.constant.MessageDispatcherType;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.server.netty.DefaultNettyServer;
import in.hocg.netty.server.netty.handler.DispatcherHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;

/**
 * @author hocgin
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageDispatcherHandler extends DispatcherHandler {

    @Override
    public void channelRead0(ChannelHandlerContext context, Packet packet) {
        String data = new String(packet.getData(), StandardCharsets.UTF_8);
        String channelId = context.channel().id().asLongText();

        Message message = MessageBuilder.withPayload(data)
            .setHeader(MessageConstant.TYPE, MessageDispatcherType.Dispatcher.name())
            .setHeader(MessageConstant.CHANNEL_ID, channelId)
            .setHeader(MessageConstant.SERVER_ID, DefaultNettyServer.ID)
            .build();

        MessageFactory.normal().asyncSend(MessageConstant.MESSAGE_TOPIC, message);
        log.debug("发送消息至MQ, Destination: {} dataString: {}", MessageConstant.MESSAGE_TOPIC, data);
    }

}
