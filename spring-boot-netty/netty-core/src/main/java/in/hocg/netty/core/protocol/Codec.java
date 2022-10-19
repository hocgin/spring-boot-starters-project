package in.hocg.netty.core.protocol;

import in.hocg.netty.core.serializer.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class Codec {
    /**
     * 编码
     *
     * @param msg
     */
    public static byte[] encode(AbstractPacket msg) {
        SerializerAlgorithm defaultSerializerAlgorithm = SerializerAlgorithm.JSON;

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = defaultSerializerAlgorithm.serializer()
            .serialize(msg);

        byteBuf.writeInt(WordConstant.Content.MAGIC_NUMBER_CONTENT);
        byteBuf.writeByte(msg.getVersion());
        byteBuf.writeByte(defaultSerializerAlgorithm.algorithm());
        byteBuf.writeByte(msg.getCommand());
        byteBuf.writeByte(msg.getModule());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return ByteBufUtil.getBytes(byteBuf);
    }

    /**
     * 解码
     *
     * @param msg
     */
    public static Packet decode(ByteBuf msg) {

        // 魔数(4)
        msg.skipBytes(WordConstant.Width.MAGIC_NUMBER);

        // 版本号(1)
        byte version = msg.readByte();

        // 序列化算法(1)
        byte algorithm = msg.readByte();

        // 模块(1)
        byte module = msg.readByte();

        // 指令(1)
        byte command = msg.readByte();

        // 数据长度(4)
        int length = msg.readInt();

        // 数据(n)
        byte[] content = new byte[length];

        msg.readBytes(content);

        return new Packet(version, algorithm, module, command, content);
    }
}
