package in.hocg.netty.core.pojo.ro;


import in.hocg.netty.core.constant.SystemPacket;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;

public class HeartBeatPacket extends Packet {

    public HeartBeatPacket() {
        super(SystemPacket.Version, SerializerAlgorithm.JSON.algorithm(), SystemPacket.SystemModule,
            SystemPacket.HEARTBEAT_PACKET, SystemPacket.EMPTY_DATA);
    }


}
