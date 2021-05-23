package in.hocg.boot.message.autoconfigure.service.normal.redis;

import cn.hutool.core.util.ClassUtil;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by hocgin on 2021/5/23
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class RedisHelper {

    public static RedisSerializer getValueSerializer() {
        return RedisSerializer.java(ClassUtil.getClassLoader());
    }

    public static RedisSerializer getKeySerializer() {
        return RedisSerializer.string();
    }

}
