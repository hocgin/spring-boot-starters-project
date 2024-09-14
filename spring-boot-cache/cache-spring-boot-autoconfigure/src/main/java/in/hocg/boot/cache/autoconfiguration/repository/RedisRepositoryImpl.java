package in.hocg.boot.cache.autoconfiguration.repository;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Sets;
import in.hocg.boot.cache.autoconfiguration.serializer.RedisKeySerializer;
import in.hocg.boot.cache.autoconfiguration.serializer.RedisObjectSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public static final RedisObjectSerializer OBJECT_SERIALIZER = new RedisObjectSerializer();

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

    public RedisSerializer<String> getRedisKeySerializer() {
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
            RedisSerializer<String> serializer = getRedisKeySerializer();
            byte[] keys = serializer.serialize(key);
            byte[] values = OBJECT_SERIALIZER.serialize(value);
            connection.setEx(keys, duration.get(ChronoUnit.SECONDS), values);
            return 1L;
        });
    }

    @Override
    public <T> void setExpire(String[] keys, T[] values, Duration duration) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisKeySerializer();
            for (int i = 0; i < keys.length; i++) {
                byte[] bKeys = serializer.serialize(keys[i]);
                byte[] bValues = OBJECT_SERIALIZER.serialize(values[i]);
                connection.setEx(bKeys, duration.get(ChronoUnit.SECONDS), bValues);
            }
            return 1L;
        });
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Long getExpire(String key, final TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public <T> void set(String[] keys, T[] values) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = getRedisKeySerializer();
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
            RedisSerializer<String> serializer = getRedisKeySerializer();
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
            RedisSerializer<String> serializer = getRedisKeySerializer();
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
        RedisSerializer<String> serializer = getRedisKeySerializer();
        return redisTemplate.execute((RedisCallback<Boolean>) connection ->
            connection.exists(serializer.serialize(key)));
    }

    @Override
    public long del(String... keys) {
        RedisSerializer<String> serializer = getRedisKeySerializer();
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
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, scanOptions)) {
            while (cursor.hasNext()) {
                Map.Entry<K, V> entry = (Map.Entry<K, V>) cursor.next();
                entrySet.add(entry);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try (Cursor<Object> cursor = redisTemplate.opsForSet().scan(key, scanOptions)) {
            while (cursor.hasNext()) {
                T entry = (T) cursor.next();
                entrySet.add(entry);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try (Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan(key, scanOptions)) {
            while (cursor.hasNext()) {
                T entry = (T) cursor.next().getValue();
                entrySet.add(entry);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try (ConvertingCursor<byte[], String> cursor = redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(connection.scan(scanOptions), new StringRedisSerializer()::deserialize))) {
            // 因为使用scan命令可能会存在重复，所以使用HashSet去重
            if (cursor != null) {
                Set<String> set = Sets.newHashSet();
                cursor.forEachRemaining(set::add);
                return set;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Collections.emptySet();
    }

    @Override
    public Boolean setNxAndExpire(String key, String value, long milliseconds) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, milliseconds, TimeUnit.MILLISECONDS);
        return null != result && result;
    }

    @Override
    public boolean setIfNull(String key, String value) {
        // Lua 脚本
        String script = "local key = KEYS[1] local value = ARGV[1] if redis.call('get', key) == false then redis.call('set', key, value) return 1 else return 0 end";

        // 执行脚本
        Long result = redisTemplate.execute((RedisCallback<Long>) (connection) -> {
            RedisSerializer<String> serializer = this.getRedisKeySerializer();
            byte[] keys = serializer.serialize(key);
            byte[] v1 = OBJECT_SERIALIZER.serialize(value);
            byte[][] args = {keys, v1};
            return connection.scriptingCommands().eval(script.getBytes(), ReturnType.INTEGER, 1, args);
        });

        // 返回操作结果，1 表示成功，0 表示失败
        return Objects.nonNull(result) && Convert.toLong(result) == 1L;
    }

    @Override
    public boolean compareAndSet(String key, String oldValue, String newValue) {
        // Lua 脚本实现 CAS 操作
        String script = "local key = KEYS[1] if redis.call('get', key) == ARGV[1] then redis.call('set', key, ARGV[2]) return 1 else return 0 end";

        // 执行脚本
        Long result = redisTemplate.execute((RedisCallback<Long>) (connection) -> {
            RedisSerializer<String> serializer = this.getRedisKeySerializer();
            byte[] keys = serializer.serialize(key);
            byte[] v1 = OBJECT_SERIALIZER.serialize(oldValue);
            byte[] v2 = OBJECT_SERIALIZER.serialize(newValue);
            byte[][] args = {keys, v1, v2};
            return connection.scriptingCommands().eval(script.getBytes(), ReturnType.INTEGER, 1, args);
        });

        // 返回操作结果，1 表示成功，0 表示失败
        return Objects.nonNull(result) && Convert.toLong(result) == 1L;
    }

    @Override
    public byte[] keyPrefix(String key) {
        return getRedisKeySerializer().serialize(key);
    }

    @Override
    public String getKeyPrefix() {
        return keyPrefix;
    }
}
