package in.hocg.netty.core.protocol;

import in.hocg.netty.core.protocol.packet.AbstractPacket;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class Codec {
    /**
     * 编码
     *
     * @param packet
     */
    public static byte[] encode(AbstractPacket packet) {
        return Codec.encode(SerializerAlgorithm.JSON, packet);
    }

    public static byte[] encode(SerializerAlgorithm serializerAlgorithm, AbstractPacket packet) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = serializerAlgorithm.serialize(packet);

        byteBuf.writeByte(WordConstant.Content.MAGIC_NUMBER_CONTENT);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(serializerAlgorithm.algorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeByte(packet.getModule());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return ByteBufUtil.getBytes(byteBuf);
    }

    /**
     * 解码
     *
     * @param bytedata
     */
    public static Packet decode(ByteBuf bytedata) {

        // 魔数(4)
        bytedata.skipBytes(WordConstant.Width.MAGIC_NUMBER);

        // 版本号(1)
        byte version = bytedata.readByte();

        // 序列化算法(1)
        byte algorithm = bytedata.readByte();

        // 模块(1)
        byte module = bytedata.readByte();

        // 指令(1)
        byte command = bytedata.readByte();

        // 数据长度(4)
        int length = bytedata.readInt();

        // 数据(n)
        byte[] content = new byte[length];

        bytedata.readBytes(content);

        return new Packet(version, algorithm, module, command, content);
    }
}
