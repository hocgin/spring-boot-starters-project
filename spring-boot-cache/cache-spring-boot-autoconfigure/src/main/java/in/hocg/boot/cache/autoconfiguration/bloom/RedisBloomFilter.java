package in.hocg.boot.cache.autoconfiguration.bloom;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class RedisBloomFilter {

    private final RedisBloomFilterStrategy bloomFilterStrategy;
    private final RedisBitMap redisBitMap;
    private final Integer numOfHashFunctions;

    private RedisBloomFilter(String bloomFilterName, long expectedInsertions, double fpp, StringRedisTemplate redisTemplate) {
        this.bloomFilterStrategy = new RedisBloomFilterStrategy();
        long bits = optimalNumOfBits(expectedInsertions, fpp);
        this.numOfHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bits);
        this.redisBitMap = new RedisBitMap(redisTemplate, bloomFilterName, bits);
    }

    public static RedisBloomFilter create(String bloomFilterName, long expectedInsertions, double fpp, StringRedisTemplate redisTemplate) {
        return new RedisBloomFilter(bloomFilterName, expectedInsertions, fpp, redisTemplate);
    }


    /**
     * 存入值
     *
     * @param value 值
     * @return 存入状态
     */
    public Boolean put(String value) {
        return this.bloomFilterStrategy.put(value, numOfHashFunctions, redisBitMap);
    }

    /**
     * 是否包含值
     *
     * @param value 值
     * @return 包含状态
     */
    public Boolean mightContain(String value) {
        return this.bloomFilterStrategy.mightContain(value, numOfHashFunctions, redisBitMap);
    }

    int optimalNumOfHashFunctions(long expectedInsertions, long numBits) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) numBits / expectedInsertions * Math.log(2)));
    }

    /**
     * 计算所需空间大小
     */
    long optimalNumOfBits(long expectedInsertions, double fpp) {
        if (fpp == 0) {
            fpp = Double.MIN_VALUE;
        }
        return (long) (-expectedInsertions * Math.log(fpp) / (Math.log(2) * Math.log(2)));
    }


}
