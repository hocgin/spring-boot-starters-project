package in.hocg.boot.cache.bloom;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class RedisBloomFilter {

    private final RedisBloomFilterStrategy redisBloomFilterStrategy;
    private final RedisBitMap redisBitMap;
    private final int numOfHashFunctions;
    private final long bits;

    private long expectedInsertions;
    private double fpp;
    private String bloomFilterName;
    private StringRedisTemplate stringRedisTemplate;

    public RedisBloomFilter(long expectedInsertions, double fpp, String bloomFilterName, StringRedisTemplate stringRedisTemplate) {
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.bloomFilterName = bloomFilterName;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisBloomFilterStrategy = new RedisBloomFilterStrategy();
        this.bits = optimalNumOfBits(expectedInsertions, fpp);
        this.numOfHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bits);
        this.redisBitMap = new RedisBitMap(stringRedisTemplate, bloomFilterName, bits);
    }


    /**
     * @param value
     * @return
     */
    public Boolean put(String value) {
        return this.redisBloomFilterStrategy.put(value, numOfHashFunctions, redisBitMap);
    }

    /**
     * @param value
     * @return
     */
    public Boolean mightContain(String value) {
        return this.redisBloomFilterStrategy.mightContain(value, numOfHashFunctions, redisBitMap);
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
