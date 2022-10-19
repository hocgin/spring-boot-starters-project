package in.hocg.boot.netty.server.autoconfiguration.core;

import in.hocg.boot.message.autoconfigure.MessageFactory;
import in.hocg.netty.core.constant.MessageConstant;
import in.hocg.netty.core.constant.MessageHeader;
import in.hocg.netty.core.protocol.Packet;
import in.hocg.netty.server.netty.handler.AbsForwardHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author hocgin
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageForwardHandler extends AbsForwardHandler {

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        byte module = packet.getModule();
        byte command = packet.getCommand();
        byte algorithm = packet.getAlgorithm();
        byte[] data = packet.getData();

        Message message = MessageBuilder.withPayload(data)
            .setHeader(MessageHeader.SOURCE, channelHandlerContext.channel().id().asLongText())
            .setHeader(MessageHeader.ALGORITHM, algorithm)
            .setHeader(MessageHeader.COMMAND, command)
            .setHeader(MessageHeader.MODULE, module)
            .build();

        MessageFactory.normal().asyncSend(MessageConstant.WORKER_TOPIC, message);
        log.debug("发送消息至MQ, Destination: {}\n data: {} \n dataString: {}", MessageConstant.WORKER_TOPIC, data, new String(data));
    }

}
