package in.hocg.netty.core.body.request;


import in.hocg.netty.core.body.constant.DefaultModule;
import in.hocg.netty.core.protocol.AbstractPacket;

public class HeartBeatRequest extends AbstractPacket {
    @Override
    public byte getCommand() {
        return DefaultModule.HEART_BEAT_REQUEST;
    }

    @Override
    public byte getModule() {
        return DefaultModule.MODULE_VALUE;
    }

}
