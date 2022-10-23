package in.hocg.boot.netty.sample.constant;

import in.hocg.netty.core.constant.SystemPacket;

/**
 * Created by hocgin on 2019/3/17.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SuppressWarnings("all")
public interface NettyConstant {
    byte MOD_TEST = SystemPacket.DefaultModule;

    /**
     * 指令
     */
    byte CMD_TEST1 = 1;
    byte CMD_PING = 2;
}

