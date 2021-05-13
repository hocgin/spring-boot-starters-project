package in.hocg.boot.cache.bloom;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class RedisBloomFilterStrategy {

    /**
     * @param value
     * @param numHashFunctions
     * @param bitArray
     * @return
     */
    public boolean put(String value, int numHashFunctions, RedisBitMap bitArray) {
        long bitSize = bitArray.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashUnencodedChars(value).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        boolean bitsChanged = false;
        long combinedHash = hash1;
        //gi(x) = h1(x) + ih2(x)  ,用两个函数模拟多个哈希函数
        for (int i = 0; i < numHashFunctions; i++) {
            // combinedHash & Long.MAX_VALUE 取正
            bitsChanged |= bitArray.set((combinedHash & Long.MAX_VALUE) % bitSize);
            combinedHash += hash2;
        }
        return bitsChanged;
    }

    /**
     * @param value
     * @param numHashFunctions
     * @param bitArray
     * @return
     */
    public boolean mightContain(String value, int numHashFunctions, RedisBitMap bitArray) {
        long bitSize = bitArray.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashUnencodedChars(value).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        long combinedHash = hash1;

        for (int i = 0; i < numHashFunctions; i++) {
            //只要有一次hash后未映射到bit数组上，则认为该元素一定不存在
            if (bitArray.set((combinedHash & Long.MAX_VALUE) % bitSize)) {
                return false;
            }
            combinedHash += hash2;
        }
        return true;
    }

    private long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
            bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private long upperEight(byte[] bytes) {
        return Longs.fromBytes(
            bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }
}
