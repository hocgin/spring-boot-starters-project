package in.hocg.netty.core.protocol.packet;

import in.hocg.netty.core.serializer.SerializerAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hocgin
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Packet implements AbstractPacket {
    /**
     * 协议版本
     */
    private final byte version;
    /**
     * 签名方式
     */
    private final byte algorithm;
    /**
     * 模块
     */
    private final byte module;
    /**
     * 命令
     */
    private final byte command;
    /**
     * 所有数据
     */
    private final byte[] data;

    public  <T> T getData(Class<T> clazz) {
        return SerializerAlgorithm.getSerializer(this.algorithm).orElseThrow().deserialize(clazz, data);
    }
}
