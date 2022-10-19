package in.hocg.netty.core.serializer.impl;


import cn.hutool.json.JSONUtil;
import in.hocg.netty.core.serializer.Serializer;
import in.hocg.netty.core.serializer.SerializerAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 * <p>
 * JSON 序列化算法实现
 *
 * @author hocgin
 */
@SuppressWarnings("all")
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON.algorithm();
    }

    @Override
    public byte[] serialize(Object object) {
        if (Objects.isNull(object)) {
            return new byte[0];
        }
        return JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
