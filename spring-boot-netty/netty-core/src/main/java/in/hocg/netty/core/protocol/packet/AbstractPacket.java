package in.hocg.netty.core.protocol.packet;

import in.hocg.netty.core.constant.SystemPacket;

import java.io.Serializable;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface AbstractPacket extends Serializable {

    default byte getVersion() {
        return SystemPacket.Version;
    }

    /**
     * 获取指令
     *
     * @return
     */
    byte getCommand();

    /**
     * 获取模块
     *
     * @return
     */
    byte getModule();

}
