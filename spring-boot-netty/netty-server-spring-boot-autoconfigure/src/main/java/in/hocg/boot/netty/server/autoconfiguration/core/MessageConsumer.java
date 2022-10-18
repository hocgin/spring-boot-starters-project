package in.hocg.boot.netty.server.autoconfiguration.core;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.message.autoconfigure.service.normal.redis.RedisMessageListener;
import in.hocg.netty.core.constant.MessageConstant;
import in.hocg.netty.core.constant.MessageHeader;
import in.hocg.netty.server.netty.session.SessionManager;
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
public class MessageConsumer extends RedisMessageListener<Message<String>> {

    @Override
    public void onMessage(Message<String> message) {
        MessageHeaders headers = message.getHeaders();
        String bodyStr = message.getPayload();
        String destination = headers.get("USERS_" + MessageHeader.DESTINATION, String.class);
        Channel channel = SessionManager.get(destination);
        if (channel == null) {
            log.debug("查找不到用户 {} \n 消息内容: {}", destination, bodyStr);
            return;
        }
        byte[] body = JSONUtil.toJsonStr(bodyStr).getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes(body);
        channel.writeAndFlush(byteBuf);
        log.debug("\n -> 消息体: {} \n -> 接收者: {}", body, destination);
    }

    @Override
    protected Topic getTopic() {
        return new PatternTopic(MessageConstant.BOSSER_TOPIC);
    }
}
