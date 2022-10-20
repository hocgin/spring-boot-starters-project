package in.hocg.netty.core.serializer;

import in.hocg.netty.core.serializer.impl.JSONSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum SerializerAlgorithm {
    /**
     * JSON 序列化算法
     */
    JSON(((byte) 1), new JSONSerializer());

    private final byte algorithm;
    private final Serializer serializer;

    /**
     * 获取序列化
     *
     * @param algorithm
     * @return
     */
    public static Optional<Serializer> getSerializer(byte algorithm) {
        for (SerializerAlgorithm serializerAlgorithm : SerializerAlgorithm.values()) {
            if (serializerAlgorithm.algorithm == algorithm) {
                return Optional.of(serializerAlgorithm.serializer);
            }
        }
        return Optional.empty();
    }

    public byte[] serialize(Object data) {
        return this.serializer.serialize(data);
    }
}
