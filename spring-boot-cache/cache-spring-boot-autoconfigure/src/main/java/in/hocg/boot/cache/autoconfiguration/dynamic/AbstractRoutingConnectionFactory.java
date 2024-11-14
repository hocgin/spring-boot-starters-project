package in.hocg.boot.cache.autoconfiguration.dynamic;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.connection.SentinelConnectionManager;
import org.redisson.spring.data.connection.RedissonClusterConnection;
import org.redisson.spring.data.connection.RedissonConnection;
import org.redisson.spring.data.connection.RedissonExceptionConverter;
import org.redisson.spring.data.connection.RedissonSentinelConnection;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;

@Slf4j
public abstract class AbstractRoutingConnectionFactory implements RedisConnectionFactory, InitializingBean, DisposableBean {
    public static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(new RedissonExceptionConverter());

    public AbstractRoutingConnectionFactory(Map<String, RedissonClient> targetDataSources) {
        this.targetDataSources = targetDataSources;
        this.hasOwnRedisson = true;
    }

    private final Map<String, RedissonClient> targetDataSources;
    @Nullable
    private Object defaultTargetDataSource;
    private @Nullable Map<Object, RedissonClient> resolvedDataSources;
    private @Nullable RedissonClient resolvedDefaultDataSource;
    private boolean hasOwnRedisson;

    @Nullable
    public RedissonClient getResolvedDefaultDataSource() {
        return resolvedDefaultDataSource;
    }

    @Override
    public RedisConnection getConnection() {
        RedissonClient redisson = determineTargetDataSource();
        return new RedissonConnection(redisson);
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        RedissonClient redisson = determineTargetDataSource();
        if (!redisson.getConfig().isClusterConfig()) {
            throw new InvalidDataAccessResourceUsageException("Redisson is not in Cluster mode");
        } else {
            return new RedissonClusterConnection(redisson);
        }
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        RedissonClient redisson = determineTargetDataSource();
        if (!redisson.getConfig().isSentinelConfig()) {
            throw new InvalidDataAccessResourceUsageException("Redisson is not in Sentinel mode");
        } else {
            SentinelConnectionManager manager = (SentinelConnectionManager) ((Redisson) redisson).getConnectionManager();
            Iterator var2 = manager.getSentinels().iterator();

            while (var2.hasNext()) {
                RedisClient client = (RedisClient) var2.next();
                org.redisson.client.RedisConnection connection = client.connect();

                try {
                    String res = connection.sync(RedisCommands.PING, new Object[0]);
                    if ("pong".equalsIgnoreCase(res)) {
                        return new RedissonSentinelConnection(connection);
                    }
                } catch (Exception var6) {
                    Exception e = var6;
                    log.warn("Can't connect to " + client, e);
                    connection.closeAsync();
                }
            }

            throw new InvalidDataAccessResourceUsageException("Sentinels are not found");
        }
    }

    public boolean getConvertPipelineAndTxResults() {
        return true;
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    @Override
    public void afterPropertiesSet() {
        if (this.targetDataSources == null) {
            throw new IllegalArgumentException("Property 'targetDataSources' is required");
        }
        this.resolvedDataSources = CollUtil.newHashMap(this.targetDataSources.size());
        this.targetDataSources.forEach((key, value) -> {
            Object lookupKey = resolveSpecifiedLookupKey(key);
            RedissonClient dataSource = resolveSpecifiedDataSource(value);
            this.resolvedDataSources.put(lookupKey, dataSource);
        });
        if (this.defaultTargetDataSource != null) {
            this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource);
        }
    }

    @Override
    public void destroy() throws Exception {
        for (Map.Entry<String, RedissonClient> entry : targetDataSources.entrySet()) {
            try {
                entry.getValue().shutdown();
            } catch (Exception e) {
                log.error("Redisson shutdown error", e);
            }
        }
    }

    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    protected RedissonClient resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof RedissonClient) {
            return (RedissonClient) dataSource;
        } else if (dataSource instanceof String) {
            RedissonClient client = this.resolvedDataSources.get(dataSource);
            return cn.hutool.core.lang.Assert.notNull(client, "{} RedissonClient not found in targetDataSources", dataSource);
        } else {
            throw new IllegalArgumentException(
                "Illegal data source value - only [RedissonConnection] and String supported: " + dataSource);
        }
    }

    public AbstractRoutingConnectionFactory setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
        return this;
    }

    protected RedissonClient determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        RedissonClient dataSource = this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && (this.hasOwnRedisson || lookupKey == null)) {
            dataSource = this.resolvedDefaultDataSource;
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }

    protected abstract @Nullable Object determineCurrentLookupKey();
}
