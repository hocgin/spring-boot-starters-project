package in.hocg.netty.core.protocol.codec;

import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by hocgin on 2019/3/5.
 * email: hocgin@gmail.com
 * 魔数(4) | 版本号(1) | 序列化算法(1) | 模块(1) | 指令(1) | 数据长度(4) | 数据(n)
 *
 * @author hocgin
 */
@Slf4j
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Packet> {

    /**
     * 编码
     *
     * @param ctx
     * @param packet
     * @param out
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
        out.add(Codec.encodeBuf(packet));
    }

    /**
     * 解码
     *
     * @param ctx
     * @param buf
     * @param out
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        out.add(Codec.decode(buf));
    }
}
