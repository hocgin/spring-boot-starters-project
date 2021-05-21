package in.hocg.boot.cache.autoconfiguration.bloom;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class RedisBitMap {
    private final StringRedisTemplate redisTemplate;
    private final String bloomFilterName;

    public RedisBitMap(StringRedisTemplate redisTemplate, String bloomFilterName, long bits) {
        this.redisTemplate = redisTemplate;
        this.bloomFilterName = bloomFilterName;
        redisTemplate.opsForValue().setBit(bloomFilterName, bits - 1, false);
    }

    /**
     * @param bitIndex
     * @return
     */
    public Boolean set(long bitIndex) {
        if (get(bitIndex)) {
            return false;
        }

        return redisTemplate.opsForValue().setBit(bloomFilterName, bitIndex, true);
    }

    /**
     * @param bitIndex
     * @return
     */
    public Boolean get(long bitIndex) {
        return redisTemplate.opsForValue().getBit(bloomFilterName, bitIndex);
    }

    /**
     * 当前array总bits
     */
    public Long bitSize() {
        return redisTemplate.opsForValue().size(bloomFilterName);
    }


}
