package in.hocg.boot.netty.server.autoconfiguration.core;

import in.hocg.boot.message.autoconfigure.service.normal.redis.RedisMessageListener;
import in.hocg.netty.core.constant.MessageConstant;
import in.hocg.netty.core.session.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener extends RedisMessageListener<Message<String>> {

    @Override
    public void onMessage(Message<String> message) {
        MessageHeaders headers = message.getHeaders();
        String channelId = headers.get(MessageConstant.CHANNEL_ID, String.class);
        String serverId = headers.get(MessageConstant.SERVER_ID, String.class);

        Channel channel = SessionManager.get(channelId);
        if (channel == null) {
            log.debug("查找不到 Channel.[ChannelId={}, Payload={}]", channelId, message.getPayload());
            return;
        }

        byte[] bytes = message.getPayload().getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes(bytes);
        channel.writeAndFlush(byteBuf);
    }

    @Override
    protected Topic getTopic() {
        return new PatternTopic(MessageConstant.MESSAGE_TOPIC);
    }
}
