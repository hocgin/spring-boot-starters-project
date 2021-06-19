package in.hocg.boot.youtube.autoconfiguration.core.datastore;

import cn.hutool.json.JSONUtil;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class RedisDataStoreFactory extends AbstractDataStoreFactory {
    private final StringRedisTemplate redisTemplate;

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new RedisDataStore<>(this, id, redisTemplate);
    }

    static class RedisDataStore<V extends Serializable> extends AbstractDataStore<V> {
        private StringRedisTemplate redisTemplate;

        public RedisDataStore(DataStoreFactory dataStoreFactory, String id, StringRedisTemplate redisTemplate) {
            super(dataStoreFactory, id);
            this.redisTemplate = redisTemplate;
        }

        private Map<String, String> getMap() {
            HashOperations<String, String, String> ops = redisTemplate.opsForHash();
            return ops.entries(this.getId());
        }

        @SneakyThrows
        private String toString(V value) {
            Object nValue = value;

            if (value instanceof StoredCredential) {
                StoredCredential tmp = (StoredCredential) value;
                nValue = new DataCredential()
                    .setRefreshToken(tmp.getRefreshToken())
                    .setExpirationTimeMilliseconds(tmp.getExpirationTimeMilliseconds())
                    .setAccessToken(tmp.getAccessToken());
            }

            return JSONUtil.toJsonStr(nValue);
        }

        @SneakyThrows
        private V toValue(String str) {
            if (Objects.isNull(str)) {
                return null;
            }

            DataCredential tmp = JSONUtil.toBean(str, DataCredential.class);
            Object nValue = new StoredCredential()
                .setRefreshToken(tmp.getRefreshToken())
                .setAccessToken(tmp.getAccessToken())
                .setExpirationTimeMilliseconds(tmp.getExpirationTimeMilliseconds());
            return (V) nValue;
        }

        /**
         * @param dataStoreFactory data store factory
         * @param id               data store ID
         */
        protected RedisDataStore(DataStoreFactory dataStoreFactory, String id) {
            super(dataStoreFactory, id);
        }

        @Override
        public int size() throws IOException {
            return this.getMap().size();
        }

        @Override
        public boolean isEmpty() throws IOException {
            return this.getMap().isEmpty();
        }

        @Override
        public boolean containsKey(String key) throws IOException {
            return this.getMap().containsKey(key);
        }

        @Override
        public boolean containsValue(V value) throws IOException {
            return this.getMap().containsValue(this.toString(value));
        }

        @Override
        public Set<String> keySet() throws IOException {
            return this.getMap().keySet();
        }

        @Override
        public Collection<V> values() throws IOException {
            return this.getMap().values().stream().map(this::toValue).collect(Collectors.toList());
        }

        @Override
        public V get(String key) throws IOException {
            return this.toValue(this.getMap().get(key));
        }

        @Override
        public DataStore<V> set(String key, V value) throws IOException {
            redisTemplate.opsForHash().put(this.getId(), key, this.toString(value));
            return this;
        }

        @Override
        public DataStore<V> clear() throws IOException {
            redisTemplate.delete(this.getId());
            return this;
        }

        @Override
        public DataStore<V> delete(String key) throws IOException {
            redisTemplate.opsForHash().delete(this.getId(), key);
            return this;
        }
    }

}
