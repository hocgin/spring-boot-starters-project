package in.hocg.netty.core.session;

import in.hocg.netty.core.protocol.codec.Codec;
import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hocgin on 2019/3/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class SessionManager {
    private static final Map<Serializable, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 登陆在线
     *
     * @param key
     * @param channel
     */
    public static Channel add(Serializable key, Channel channel) {
        return CHANNEL_MAP.put(key, channel);
    }

    /**
     * 移除在线
     *
     * @param key
     */
    public static Channel remove(Serializable key) {
        return CHANNEL_MAP.remove(key);
    }

    /**
     * 获取 Channel
     *
     * @param key
     * @return
     */
    public static Channel get(Serializable key) {
        return CHANNEL_MAP.get(key);
    }

    public static boolean send(Serializable channelId, Packet packet) {
        Channel channel = SessionManager.get(channelId);
        if (channel == null) {
            log.debug("查找不到用户 {} \n 消息内容: {}", channelId, packet);
            return false;
        }
        byte[] body = Codec.encode(packet);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes(body);
        channel.writeAndFlush(byteBuf);
        return true;
    }
}
