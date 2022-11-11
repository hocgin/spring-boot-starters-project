package in.hocg.boot.cache.autoconfiguration;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.autoconfiguration.aspect.DistributeLockAspect;
import in.hocg.boot.cache.autoconfiguration.aspect.NoRepeatSubmitAspect;
import in.hocg.boot.cache.autoconfiguration.lock.DistributedLock;
import in.hocg.boot.cache.autoconfiguration.lock.RedissonDistributedLock;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hocgin
 */
@Configuration
@ConditionalOnClass({Redisson.class})
@AutoConfigureBefore({org.redisson.spring.starter.RedissonAutoConfiguration.class, CacheAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {
    private final RedissonProperties properties;

    /**
     * 单机模式
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "Single")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        RedissonProperties.RedisSingleProperties single = properties.getSingle();
        String node = prefixAddress(single.getAddress());
        RedissonProperties.RedisPoolProperties pool = properties.getPool();
        SingleServerConfig serverConfig = config.useSingleServer().setAddress(node);
        LangUtils.callIfNotNull(pool.getConnTimeout(), serverConfig::setConnectTimeout);
        LangUtils.callIfNotNull(pool.getSize(), serverConfig::setConnectionPoolSize);
        LangUtils.callIfNotNull(pool.getMinIdle(), serverConfig::setConnectionMinimumIdleSize);
        LangUtils.callIfNotNull(properties.getPassword(), serverConfig::setPassword);
        return Redisson.create(config);
    }

    /**
     * 集群模式
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "Cluster")
    public RedissonClient redissonCluster() {
        RedissonProperties.RedisClusterProperties cluster = properties.getCluster();
        RedissonProperties.RedisPoolProperties pool = properties.getPool();
        System.out.println("cluster redisProperties:" + cluster);
        Config config = new Config();
        String[] nodes = StrUtil.split(cluster.getNodes(), ",");
        List<String> newNodes = new ArrayList<>(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(prefixAddress(index)));

        ClusterServersConfig serverConfig = config.useClusterServers()
            .addNodeAddress(newNodes.toArray(new String[0]));
        LangUtils.callIfNotNull(cluster.getScanInterval(), serverConfig::setScanInterval);
        LangUtils.callIfNotNull(pool.getSoTimeout(), serverConfig::setIdleConnectionTimeout);
        LangUtils.callIfNotNull(pool.getConnTimeout(), serverConfig::setConnectTimeout);
        LangUtils.callIfNotNull(cluster.getRetryAttempts(), serverConfig::setRetryAttempts);
        LangUtils.callIfNotNull(cluster.getRetryInterval(), serverConfig::setRetryInterval);
        LangUtils.callIfNotNull(cluster.getMasterConnectionPoolSize(), serverConfig::setMasterConnectionPoolSize);
        LangUtils.callIfNotNull(cluster.getSlaveConnectionPoolSize(), serverConfig::setSlaveConnectionPoolSize);
        LangUtils.callIfNotNull(properties.getTimeout(), serverConfig::setTimeout);
        LangUtils.callIfNotNull(properties.getPassword(), serverConfig::setPassword);
        return Redisson.create(config);
    }

    /**
     * 哨兵模式
     */
    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "Sentinel")
    public RedissonClient redissonSentinel() {
        RedissonProperties.RedisSentinelProperties sentinel = properties.getSentinel();
        RedissonProperties.RedisPoolProperties pool = properties.getPool();
        System.out.println("sentinel redisProperties:" + sentinel);
        Config config = new Config();
        String[] nodes = StrUtil.split(sentinel.getNodes(), ",");
        List<String> newNodes = new ArrayList<>(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(prefixAddress(index)));

        SentinelServersConfig serverConfig = config.useSentinelServers()
            .addSentinelAddress(newNodes.toArray(new String[0]));
        LangUtils.callIfNotNull(sentinel.getMaster(), serverConfig::setMasterName);
        LangUtils.callIfNotNull(properties.getTimeout(), serverConfig::setTimeout);
        LangUtils.callIfNotNull(pool.getSize(), serverConfig::setMasterConnectionPoolSize);
        LangUtils.callIfNotNull(pool.getSize(), serverConfig::setSlaveConnectionPoolSize);
        serverConfig.setReadMode(ReadMode.SLAVE);
        LangUtils.callIfNotNull(properties.getPassword(), serverConfig::setPassword);
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributeLockAspect distributedLockAspect(DistributedLock distributedLock) {
        return new DistributeLockAspect(properties, distributedLock);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({CacheRepository.class})
    public NoRepeatSubmitAspect noRepeatSubmitAspect(CacheRepository repository) {
        return new NoRepeatSubmitAspect(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLock distributedLock(RedissonClient redissonClient) {
        return new RedissonDistributedLock(redissonClient);
    }

    private String prefixAddress(String address) {
        if (StrUtil.isNotBlank(address) && (!address.startsWith("redis://") || !address.startsWith("rediss://"))) {
            return "redis://" + address;
        }
        return address;
    }


}
