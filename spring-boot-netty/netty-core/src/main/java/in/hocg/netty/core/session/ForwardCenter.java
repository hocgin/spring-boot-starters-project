package in.hocg.netty.core.session;

import in.hocg.netty.core.protocol.packet.Packet;

import java.io.Serializable;

/**
 * @author hocgin
 */
public class ForwardCenter {
    public static void sendAsync(SessionManager.ChannelType chanelType, Serializable channelId, Packet packet) {
        if (SessionManager.ChannelType.Server.equals(chanelType) || SessionManager.ChannelType.Client.equals(chanelType)) {
            SessionManager.send(chanelType, channelId, packet);
        } else {
            SessionManager.send(channelId, packet);
        }
    }

    public static void sendSync(Object channelId, Packet packet) {
        // 消息异步转同步
    }
}
