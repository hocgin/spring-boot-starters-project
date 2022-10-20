package in.hocg.netty.core.pojo.ro;


import in.hocg.netty.core.constant.SystemPacketConstant;
import in.hocg.netty.core.protocol.packet.AbstractPacket;

public class HeartBeatRequest implements AbstractPacket {

    @Override
    public byte getCommand() {
        return SystemPacketConstant.HEART_BEAT_REQUEST;
    }

    @Override
    public byte getModule() {
        return SystemPacketConstant.SystemModule;
    }

}
