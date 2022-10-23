package in.hocg.netty.core.constant;

/**
 * 默认模块
 *
 * @author hocgin
 */
public interface SystemPacket {
    byte Version = 1;
    byte SystemModule = 0;
    byte DefaultModule = 1;

    byte HEARTBEAT_PACKET = 0;

    byte[] EMPTY_DATA = {};
}
