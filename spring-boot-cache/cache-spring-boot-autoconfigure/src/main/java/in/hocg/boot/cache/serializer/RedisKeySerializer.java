package in.hocg.boot.cache.serializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class RedisKeySerializer implements RedisSerializer<String> {
    private final Charset charset = StandardCharsets.UTF_8;
    private final String keyPrefix;

    public RedisKeySerializer(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public String deserialize(byte[] bytes) {
        String saveKey = new String(bytes, charset);
        int indexOf = saveKey.indexOf(keyPrefix);
        if (indexOf > 0) {
            log.info("key缺少前缀");
        } else {
            saveKey = saveKey.substring(indexOf);
        }
        log.debug("saveKey:{}", saveKey);
        return saveKey;
    }

    @Override
    public byte[] serialize(String string) {
        String key = keyPrefix + string;
        log.debug("key:{},getBytes:{}", key, key.getBytes(charset));
        return key.getBytes(charset);
    }
}
