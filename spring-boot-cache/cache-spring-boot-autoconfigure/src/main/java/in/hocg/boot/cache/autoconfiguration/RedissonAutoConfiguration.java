package in.hocg.boot.cache.autoconfiguration;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.autoconfiguration.aspect.DistributeLockAspect;
import in.hocg.boot.cache.autoconfiguration.aspect.NoRepeatSubmitAspect;
import in.hocg.boot.cache.autoconfiguration.aspect.RateLimitAspect;
import in.hocg.boot.cache.autoconfiguration.queue.RedisDelayedQueue;
import in.hocg.boot.cache.autoconfiguration.lock.DistributedLock;
import in.hocg.boot.cache.autoconfiguration.lock.RedissonDistributedLock;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.utils.LangUtils;
import jodd.util.StringPool;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.BaseConfig;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.connection.balancer.LoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Objects;

/**
 * @author hocgin
 */
@Configuration
@ConditionalOnClass({Redisson.class})
@AutoConfigureBefore({CacheAutoConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {
    private final RedissonProperties properties;
    private final RedisProperties redisProperties;

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        Config config = new Config();
        configGlobal(config, properties);
        switch (properties.getMode()) {
            case Single:
                configSingle(config);
                break;
            case Sentinel:
                configSentinel(config);
                break;
            case Cluster:
                configCluster(config);
                break;
            default:
                throw new IllegalArgumentException("illegal redisson type: " + properties.getMode());
        }
        return Redisson.create(config);
    }

    private void configCluster(Config config) {
        RedissonProperties.ClusterProperties props = properties.getCluster();
        ClusterServersConfig serverConfig = config.useClusterServers();
        configBaseConfig(serverConfig, properties.getGlobal());
        configMasterSlaveServerConfig(serverConfig, props);
        LangUtils.runIfNotNull(props.getScanInterval(), serverConfig::setScanInterval);
        for (String nodeAddress : StrUtil.split(props.getAddress(), ",")) {
            serverConfig.addNodeAddress(prefixAddress(nodeAddress));
        }
        serverConfig.setPassword(this.getPassword());
    }

    private void configSentinel(Config config) {
        RedissonProperties.SentinelProperties props = properties.getSentinel();
        SentinelServersConfig serverConfig = config.useSentinelServers();
        configBaseConfig(serverConfig, properties.getGlobal());
        configMasterSlaveServerConfig(serverConfig, props);
        for (String nodeAddress : StrUtil.split(props.getAddress(), ",")) {
            serverConfig.addSentinelAddress(prefixAddress(nodeAddress));
        }
        LangUtils.runIfNotNull(props.getMaster(), serverConfig::setMasterName);
        serverConfig.setDatabase(this.getDatabase());
        serverConfig.setPassword(this.getPassword());
    }

    private void configSingle(Config config) {
        RedissonProperties.SingleProperties single = properties.getSingle();
        boolean useRedisson = Objects.nonNull(single);
        String address = useRedisson ? single.getAddress() : (redisProperties.getHost() + StringPool.COLON + redisProperties.getPort());

        SingleServerConfig serverConfig = config.useSingleServer();
        configBaseConfig(serverConfig, properties.getGlobal());
        serverConfig.setAddress(prefixAddress(address));
        serverConfig.setDatabase(this.getDatabase());
        serverConfig.setPassword(this.getPassword());
        if (Objects.nonNull(single)) {
            LangUtils.runIfNotNull(single.getSubscriptionConnectionMinimumIdleSize(), serverConfig::setSubscriptionConnectionMinimumIdleSize);
            LangUtils.runIfNotNull(single.getSubscriptionConnectionPoolSize(), serverConfig::setSubscriptionConnectionPoolSize);
            LangUtils.runIfNotNull(single.getDnsMonitoringInterval(), serverConfig::setDnsMonitoringInterval);
        }
    }

    private void configBaseConfig(BaseConfig baseConfig, RedissonProperties.BaseProperties props) {
        if (Objects.isNull(props)) {
            return;
        }

        LangUtils.runIfNotNull(props.getSubscriptionsPerConnection(), baseConfig::setSubscriptionsPerConnection);
        LangUtils.runIfNotNull(props.getRetryAttempts(), baseConfig::setRetryAttempts);
        LangUtils.runIfNotNull(props.getRetryInterval(), baseConfig::setRetryInterval);
        LangUtils.runIfNotNull(props.getTimeout(), baseConfig::setTimeout);
        LangUtils.runIfNotNull(props.getClientName(), baseConfig::setClientName);
        LangUtils.runIfNotNull(props.getConnectTimeout(), baseConfig::setConnectTimeout);
        LangUtils.runIfNotNull(props.getSslKeystorePassword(), baseConfig::setSslKeystorePassword);
        LangUtils.runIfNotNull(props.getSslEnableEndpointIdentification(), baseConfig::setSslEnableEndpointIdentification);
        LangUtils.runIfNotNull(props.getSslTruststorePassword(), baseConfig::setSslTruststorePassword);
        LangUtils.runIfNotNull(props.getPingConnectionInterval(), baseConfig::setPingConnectionInterval);
        LangUtils.runIfNotNull(props.getKeepAlive(), baseConfig::setKeepAlive);
        LangUtils.runIfNotNull(props.getTcpNoDelay(), baseConfig::setTcpNoDelay);
        LangUtils.runIfNotNull(props.getSslProvider(), baseConfig::setSslProvider);
        LangUtils.runIfNotNull(props.getSslKeystore(), baseConfig::setSslKeystore);
        LangUtils.runIfNotNull(props.getIdleConnectionTimeout(), baseConfig::setIdleConnectionTimeout);
        LangUtils.runIfNotNull(props.getSslTruststore(), baseConfig::setSslTruststore);
    }

    private void configMasterSlaveServerConfig(BaseMasterSlaveServersConfig serverConfig, RedissonProperties.MasterSlaveProperties props) {
        if (Objects.isNull(props)) {
            return;
        }
        LangUtils.runIfNotNull(props.getMasterConnectionPoolSize(), serverConfig::setMasterConnectionPoolSize);
        LangUtils.runIfNotNull(props.getSlaveConnectionPoolSize(), serverConfig::setSlaveConnectionPoolSize);
        LangUtils.runIfNotNull(props.getMasterConnectionMinimumIdleSize(), serverConfig::setMasterConnectionMinimumIdleSize);
        LangUtils.runIfNotNull(props.getFailedSlaveCheckInterval(), serverConfig::setFailedSlaveCheckInterval);
        LangUtils.runIfNotNull(props.getSlaveConnectionMinimumIdleSize(), serverConfig::setSlaveConnectionMinimumIdleSize);
        LangUtils.runIfNotNull(props.getFailedSlaveReconnectionInterval(), serverConfig::setFailedSlaveReconnectionInterval);
        LangUtils.runIfNotNull(props.getSubscriptionMode(), serverConfig::setSubscriptionMode);
        LangUtils.runIfNotNull(props.getReadMode(), serverConfig::setReadMode);
        LangUtils.runIfNotNull(props.getSubscriptionConnectionMinimumIdleSize(), serverConfig::setSubscriptionConnectionMinimumIdleSize);
        LangUtils.runIfNotNull(props.getSubscriptionConnectionPoolSize(), serverConfig::setSubscriptionConnectionPoolSize);
        LangUtils.runIfNotNull(props.getDnsMonitoringInterval(), serverConfig::setDnsMonitoringInterval);
        LangUtils.runIfNotNull(props.getLoadBalancer(), clazzName -> serverConfig.setLoadBalancer((LoadBalancer) Class.forName(clazzName).getDeclaredConstructor().newInstance()));
    }

    private void configGlobal(Config config, RedissonProperties props) {
        RedissonProperties.GlobalProperties globalProps = props.getGlobal();
        if (Objects.isNull(globalProps)) {
            return;
        }

        LangUtils.runIfNotNull(globalProps.getCodec(), className -> config.setCodec((Codec) Class.forName(className).getDeclaredConstructor().newInstance()));
        LangUtils.runIfNotNull(globalProps.getTransportMode(), config::setTransportMode);
        LangUtils.runIfNotNull(globalProps.getThreads(), config::setThreads);
        LangUtils.runIfNotNull(globalProps.getNettyThreads(), config::setNettyThreads);
        LangUtils.runIfNotNull(globalProps.getNettyThreads(), config::setNettyThreads);
        LangUtils.runIfNotNull(props.getLockWatchdogTimeout(), config::setLockWatchdogTimeout);
        LangUtils.runIfNotNull(globalProps.getMinCleanUpDelay(), config::setMinCleanUpDelay);
        LangUtils.runIfNotNull(globalProps.getMaxCleanUpDelay(), config::setMaxCleanUpDelay);
        LangUtils.runIfNotNull(globalProps.getReferenceEnabled(), config::setReferenceEnabled);
        LangUtils.runIfNotNull(globalProps.getKeepPubSubOrder(), config::setKeepPubSubOrder);
        LangUtils.runIfNotNull(globalProps.getDecodeInExecutor(), config::setDecodeInExecutor);
        LangUtils.runIfNotNull(globalProps.getUseScriptCache(), config::setUseScriptCache);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributeLockAspect distributedLockAspect(DistributedLock distributedLock) {
        return new DistributeLockAspect(properties, distributedLock);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RedissonClient.class})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public RateLimitAspect rateLimitAspect(RedissonClient redissonClient) {
        return new RateLimitAspect(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnBean({CacheRepository.class})
    public NoRepeatSubmitAspect noRepeatSubmitAspect(CacheRepository repository) {
        return new NoRepeatSubmitAspect(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLock distributedLock(RedissonClient redissonClient) {
        return new RedissonDistributedLock(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisDelayedQueue delayedQueue(RedissonClient redissonClient) {
        return new RedisDelayedQueue(redissonClient);
    }

    private String prefixAddress(String address) {
        if (StrUtil.isNotBlank(address) && (!address.startsWith("redis://") || !address.startsWith("rediss://"))) {
            return "redis://" + address;
        }
        return address;
    }

    private String getPassword() {
        return StrUtil.nullToDefault(properties.getPassword(), redisProperties.getPassword());
    }

    private Integer getDatabase() {
        return ObjectUtil.defaultIfNull(properties.getDatabase(), redisProperties.getDatabase());
    }

}
