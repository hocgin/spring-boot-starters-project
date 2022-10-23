package in.hocg.boot.cache.autoconfiguration.repository;

import com.google.common.collect.Sets;
import in.hocg.boot.cache.autoconfiguration.serializer.RedisKeySerializer;
import in.hocg.boot.cache.autoconfiguration.serializer.RedisObjectSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by hocgin on 2021/5/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class RedisRepositoryImpl implements CacheRepository {

    /**
     * 值序列化器
     */
    private static final RedisObjectSerializer OBJECT_SERIALIZER = new RedisObjectSerializer();

    private final RedisTemplate<String, Object> redisTemplate;

    private final String keyPrefix;


    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    public RedisConnectionFactory getConnectionFactory() {
        return this.getRedisTemplate().getConnectionFactory();
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    protected RedisSerializer<String> getRedisSerializer() {
        return new RedisKeySerializer(keyPrefix);
    }

    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    @Override
    public void setExpire(byte[] key, byte[] value, long time) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.setEx(key, time, value);
            return 1L;
        });
    }

    @Override
    public <T> void setExpire(String key, T value, Duration duration) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = OBJECT_SERIALIZER.serialize(value);
            connection.setEx(keys, duration.get(ChronoUnit.SECONDS), values);
            return 1L;
        });
    }

    @Override
    public <T> void setExpire(String[] keys, T[] values, Duration duration) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            for (int i = 0; i < keys.length; i++) {
                byte[] bKeys = serializer.serialize(keys[i]);
                byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
                connection.setEx(bKeys, duration.get(ChronoUnit.SECONDS), bValues);
            }
            return 1L;
        });
    }

    @Override
    public <T> void set(String[] keys, T[] values) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            for (int i = 0; i < keys.length; i++) {
                byte[] bKeys = serializer.serialize(keys[i]);
                byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
                connection.set(bKeys, bValues);
            }
            return 1L;
        });
    }

    @Override
    public <T> void set(String key, T value) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = OBJECT_SERIALIZER.serialize(value);
            connection.set(keys, values);
            return 1L;
        });
    }

    @Override
    public byte[] get(byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    }

    @Override
    public <T> T get(String key) {
        return redisTemplate.execute((RedisCallback<T>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = connection.get(keys);
            return (T) OBJECT_SERIALIZER.deserialize(values);
        });
    }


    @Override
    public <T> void putHashValue(String key, String hashKey, T hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    @Override
    public <T> T getHashValues(String key, String hashKey) {
        return (T) opsForHash().get(key, hashKey);
    }

    @Override
    public void delHashValues(String key, Object... hashKeys) {
        opsForHash().delete(key, hashKeys);
    }

    @Override
    public Map<String, Object> getHashValue(String key) {
        return opsForHash().entries(key);
    }

    @Override
    public void putHashValues(String key, Map<String, Object> map) {
        opsForHash().putAll(key, map);
    }

    @Override
    public boolean exists(String key) {
        RedisSerializer<String> serializer = getRedisSerializer();
        return redisTemplate.execute((RedisCallback<Boolean>) connection ->
            connection.exists(serializer.serialize(key)));
    }

    @Override
    public long del(String... keys) {
        RedisSerializer<String> serializer = getRedisSerializer();
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long result = 0;
            for (String key : keys) {
                result = connection.del(serializer.serialize(key));
            }
            return result;
        });
    }

    @Override
    public <T> Long leftPush(String key, T value) {
        return opsForList().leftPush(key, value);
    }

    @Override
    public <T> T leftPop(String key) {
        return (T) opsForList().leftPop(key);
    }

    @Override
    public <T> Long in(String key, T value) {
        return opsForList().rightPush(key, value);
    }

    @Override
    public <T> T rightPop(String key) {
        return (T) opsForList().rightPop(key);
    }

    @Override
    public Long length(String key) {
        return opsForList().size(key);
    }

    @Override
    public <T> void remove(String key, long i, T value) {
        opsForList().remove(key, i, value);
    }

    @Override
    public <T> void set(String key, long index, T value) {
        opsForList().set(key, index, value);
    }

    @Override
    public <T> List<T> getList(String key, int start, int end) {
        return (List<T>) opsForList().range(key, start, end);
    }

    @Override
    public Long leftPushAll(String key, List<String> list) {
        return opsForList().leftPushAll(key, list);
    }

    @Override
    public <T> void insert(String key, long index, T value) {
        opsForList().set(key, index, value);
    }

    @Override
    public <K, V> Set<Map.Entry<K, V>> hScan(String key, String pattern, int count) {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, scanOptions);
        while (cursor.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) cursor.next();
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override
    public <T> Set<T> sScan(String key, String pattern, int count) {
        Set<T> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(key, scanOptions);
        while (cursor.hasNext()) {
            T entry = (T) cursor.next();
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override
    public <T> Set<T> zScan(String key, String pattern, int count) {
        Set<T> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan(key, scanOptions);
        while (cursor.hasNext()) {
            T entry = (T) cursor.next().getValue();
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override
    public Set<String> scan(String pattern, int count) {
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }

        // SCAN命令需要在同一条连接上执行
        ConvertingCursor<byte[], String> cursor = redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(connection.scan(scanOptions), new StringRedisSerializer()::deserialize));

        // 因为使用scan命令可能会存在重复，所以使用HashSet去重
        if (cursor != null) {
            Set<String> set = Sets.newHashSet();
            cursor.forEachRemaining(set::add);
            return set;
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptySet();
    }
}
