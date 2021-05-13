package in.hocg.boot.cache.repository;

import com.google.common.collect.Sets;
import in.hocg.boot.cache.serializer.RedisKeySerializer;
import in.hocg.boot.cache.serializer.RedisObjectSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
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
    public void setExpire(String key, Object value, long time) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = OBJECT_SERIALIZER.serialize(value);
            connection.setEx(keys, time, values);
            return 1L;
        });
    }

    @Override
    public void setExpire(String[] keys, Object[] values, long time) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            for (int i = 0; i < keys.length; i++) {
                byte[] bKeys = serializer.serialize(keys[i]);
                byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
                connection.setEx(bKeys, time, bValues);
            }
            return 1L;
        });
    }

    @Override
    public void set(String[] keys, Object[] values) {
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
    public void set(String key, Object value) {
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
    public Object get(String key) {
        return redisTemplate.execute((RedisCallback<Object>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = connection.get(keys);
            return OBJECT_SERIALIZER.deserialize(values);
        });
    }


    @Override
    public void putHashValue(String key, String hashKey, Object hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    @Override
    public Object getHashValues(String key, String hashKey) {
        return opsForHash().get(key, hashKey);
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
    public Long leftPush(String key, Object value) {
        return opsForList().leftPush(key, value);
    }

    @Override
    public Object leftPop(String key) {
        return opsForList().leftPop(key);
    }

    @Override
    public Long in(String key, Object value) {
        return opsForList().rightPush(key, value);
    }

    @Override
    public Object rightPop(String key) {
        return opsForList().rightPop(key);
    }

    @Override
    public Long length(String key) {
        return opsForList().size(key);
    }

    @Override
    public void remove(String key, long i, Object value) {
        opsForList().remove(key, i, value);
    }

    @Override
    public void set(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    @Override
    public List<Object> getList(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    @Override
    public Long leftPushAll(String key, List<String> list) {
        return opsForList().leftPushAll(key, list);
    }

    @Override
    public void insert(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    @Override
    public Set<Map.Entry<Object, Object>> hScan(String key, String pattern, int count) {
        Set<Map.Entry<Object, Object>> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, scanOptions);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override
    public Set<Object> sScan(String key, String pattern, int count) {
        Set<Object> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<Object> cursor = redisTemplate.opsForSet().scan(key, scanOptions);
        while (cursor.hasNext()) {
            Object entry = cursor.next();
            entrySet.add(entry);
        }
        return entrySet;
    }

    @Override
    public Set<Object> zScan(String key, String pattern, int count) {
        Set<Object> entrySet = new HashSet<>();
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan(key, scanOptions);
        while (cursor.hasNext()) {
            Object entry = cursor.next().getValue();
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
