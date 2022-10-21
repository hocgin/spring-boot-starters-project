package in.hocg.netty.core.session;

import in.hocg.netty.core.protocol.codec.Codec;
import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hocgin on 2019/3/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class SessionManager {
    private static final Map<Serializable, Channel> SERVER_CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<Serializable, Channel> CLIENT_CHANNEL_MAP = new ConcurrentHashMap<>();


    public enum ChannelType {
        Auto,
        Server,
        Client,
    }


    /**
     * 登陆在线
     *
     * @param channelId
     * @param channel
     */
    public static Channel add(ChannelType chanelType, Serializable channelId, Channel channel) {
        if (ChannelType.Server.equals(chanelType)) {
            return SERVER_CHANNEL_MAP.put(channelId, channel);
        } else {
            return CLIENT_CHANNEL_MAP.put(channelId, channel);
        }
    }

    /**
     * 移除在线
     *
     * @param channelId
     */
    public static Channel remove(ChannelType chanelType, Serializable channelId) {
        if (ChannelType.Server.equals(chanelType)) {
            return SERVER_CHANNEL_MAP.remove(channelId);
        } else {
            return CLIENT_CHANNEL_MAP.remove(channelId);
        }
    }

    /**
     * 获取 Channel
     *
     * @param channelId
     * @return
     */
    public static Channel get(Serializable channelId) {
        Channel channel = get(ChannelType.Server, channelId);
        if (Objects.isNull(channel)) {
            channel = get(ChannelType.Client, channelId);
        }
        return channel;
    }

    public static Channel get(ChannelType chanelType, Serializable channelId) {
        if (ChannelType.Server.equals(chanelType)) {
            return SERVER_CHANNEL_MAP.get(channelId);
        } else {
            return CLIENT_CHANNEL_MAP.get(channelId);
        }
    }

    public static boolean send(Serializable channelId, Packet packet) {
        boolean isOk = send(ChannelType.Server, channelId, packet);
        if (!isOk) {
            isOk = send(ChannelType.Client, channelId, packet);
        }
        return isOk;
    }

    public static boolean send(ChannelType chanelType, Serializable channelId, Packet packet) {
        Channel channel = SessionManager.get(chanelType, channelId);
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
