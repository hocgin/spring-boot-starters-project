package in.hocg.netty.core.protocol.codec;

import in.hocg.netty.core.protocol.WordConstant;
import in.hocg.netty.core.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class Codec {
    /**
     * 编码
     *
     * @param packet
     */
    public static ByteBuf encodeBuf(Packet packet) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeInt(WordConstant.Content.MAGIC_NUMBER_CONTENT);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(packet.getAlgorithm());
        byteBuf.writeByte(packet.getModule());
        byteBuf.writeByte(packet.getCommand());
        byte[] bytes = packet.getData();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public static byte[] encode(Packet packet) {
        return ByteBufUtil.getBytes(encodeBuf(packet));
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
        byte[] data = new byte[length];

        bytedata.readBytes(data);

        return new Packet(version, algorithm, module, command, data);
    }
}
