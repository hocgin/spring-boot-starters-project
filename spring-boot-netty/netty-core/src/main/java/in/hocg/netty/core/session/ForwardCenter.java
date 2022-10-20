package in.hocg.netty.core.session;

import in.hocg.netty.core.protocol.packet.Packet;

import java.io.Serializable;

public class ForwardCenter {
    public static void sendAsync(Serializable channelId, Packet packet) {
        SessionManager.send(channelId, packet);
        // 本地没有发送给消息中心统一做转发
    }

    public static void sendSync(Object channelId, Packet packet) {
        // 消息异步转同步
    }
}
