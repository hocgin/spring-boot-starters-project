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
     * 标记指定位置
     *
     * @param bitIndex 位置下标
     * @return 保存结果
     */
    public Boolean set(long bitIndex) {
        if (get(bitIndex)) {
            return false;
        }

        return redisTemplate.opsForValue().setBit(bloomFilterName, bitIndex, true);
    }

    /**
     * 查询指定位置
     *
     * @param bitIndex 位置下标
     * @return 查询结果
     */
    public Boolean get(long bitIndex) {
        return redisTemplate.opsForValue().getBit(bloomFilterName, bitIndex);
    }

    /**
     * 当前总位图大小
     */
    public Long bitSize() {
        return redisTemplate.opsForValue().size(bloomFilterName);
    }


}
