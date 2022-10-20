package in.hocg.netty.client;

import in.hocg.netty.core.protocol.packet.AbstractPacket;
import in.hocg.netty.core.protocol.Codec;
import in.hocg.netty.core.protocol.WordConstant;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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
public class MessageCodec extends MessageToMessageCodec<ByteBuf, AbstractPacket> {

    /**
     * 编码
     *
     * @param ctx
     * @param msg
     * @param out
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) {
        SerializerAlgorithm defaultSerializerAlgorithm = SerializerAlgorithm.JSON;

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = defaultSerializerAlgorithm.serializer()
            .serialize(msg);

        byteBuf.writeInt(WordConstant.Content.MAGIC_NUMBER_CONTENT);
        byteBuf.writeByte(msg.getVersion());
        byteBuf.writeByte(defaultSerializerAlgorithm.algorithm());
        byteBuf.writeByte(msg.getModule());
        byteBuf.writeByte(msg.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        out.add(byteBuf);
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
