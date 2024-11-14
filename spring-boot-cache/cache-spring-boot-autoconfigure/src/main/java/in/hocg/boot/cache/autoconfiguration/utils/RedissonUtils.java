package in.hocg.boot.cache.autoconfiguration.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.autoconfiguration.enums.RedisMode;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonDatasourceConfig;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;
import in.hocg.boot.utils.LangUtils;
import jodd.util.StringPool;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.redisson.connection.balancer.LoadBalancer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.util.List;
import java.util.Objects;

public class RedissonUtils {

    private static String prefixAddress(String address) {
        if (StrUtil.isNotBlank(address) && (!address.startsWith("redis://") && !address.startsWith("rediss://"))) {
            return "redis://" + address;
        }
        return address;
    }

    public static RedissonClient redissonClient(RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        Config config = new Config();
        configGlobal(config, properties);
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        RedisMode mode = RedisMode.Single;
        if (Objects.nonNull(cluster)) {
            mode = RedisMode.Cluster;
        } else if (Objects.nonNull(sentinel)) {
            mode = RedisMode.Sentinel;
        }

        switch (mode) {
            case Single:
                configSingle(config, properties, redisProperties);
                break;
            case Sentinel:
                configSentinel(config, properties, redisProperties);
                break;
            case Cluster:
                configCluster(config, properties, redisProperties);
                break;
            default:
                throw new IllegalArgumentException("illegal redisson type: " + mode);
        }
        return Redisson.create(config);
    }

    private static void configCluster(Config config, RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        RedissonProperties.ClusterProperties props = ObjectUtil.defaultIfNull(properties.getCluster(), new RedissonProperties.ClusterProperties());
        ClusterServersConfig serverConfig = config.useClusterServers();
        configBaseConfig(serverConfig, properties.getGlobal());
        configMasterSlaveServerConfig(serverConfig, props);
        Integer scanInterval = props.getScanInterval();
        LangUtils.runIfNotNull(scanInterval, serverConfig::setScanInterval);
        List<String> address = StrUtil.split(props.getAddress(), ',');
        List<String> addressList = CollUtil.defaultIfEmpty(address, redisProperties.getCluster().getNodes());
        for (String nodeAddress : addressList) {
            serverConfig.addNodeAddress(prefixAddress(nodeAddress));
        }
        String password = getPassword(properties, redisProperties);
        if (StrUtil.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }
    }

    private static void configSentinel(Config config, RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        RedissonProperties.SentinelProperties props = ObjectUtil.defaultIfNull(properties.getSentinel(), new RedissonProperties.SentinelProperties());
        SentinelServersConfig serverConfig = config.useSentinelServers();
        configBaseConfig(serverConfig, properties.getGlobal());
        configMasterSlaveServerConfig(serverConfig, props);

        List<String> address = StrUtil.split(props.getAddress(), ',');
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        List<String> addressList = CollUtil.defaultIfEmpty(address, sentinel.getNodes());
        for (String nodeAddress : addressList) {
            serverConfig.addSentinelAddress(prefixAddress(nodeAddress));
        }

        LangUtils.runIfNotNull(props.getMaster(), serverConfig::setMasterName);
        serverConfig.setDatabase(getDatabase(properties, redisProperties));
        String password = getPassword(properties, redisProperties);
        if (StrUtil.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }
    }

    private static void configSingle(Config config, RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        RedissonProperties.SingleProperties single = properties.getSingle();
        boolean useRedisson = Objects.nonNull(single);
        String address = useRedisson ? single.getAddress() : (redisProperties.getHost() + StringPool.COLON + redisProperties.getPort());

        SingleServerConfig serverConfig = config.useSingleServer();
        configBaseConfig(serverConfig, properties.getGlobal());
        serverConfig.setAddress(prefixAddress(address));
        serverConfig.setDatabase(getDatabase(properties, redisProperties));
        String password = getPassword(properties, redisProperties);
        if (StrUtil.isNotBlank(password)) {
            serverConfig.setPassword(password);
        }
        if (Objects.nonNull(single)) {
            LangUtils.runIfNotNull(single.getSubscriptionConnectionMinimumIdleSize(), serverConfig::setSubscriptionConnectionMinimumIdleSize);
            LangUtils.runIfNotNull(single.getSubscriptionConnectionPoolSize(), serverConfig::setSubscriptionConnectionPoolSize);
            LangUtils.runIfNotNull(single.getDnsMonitoringInterval(), serverConfig::setDnsMonitoringInterval);
        }
    }

    private static void configBaseConfig(BaseConfig baseConfig, RedissonProperties.BaseProperties props) {
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

    private static void configMasterSlaveServerConfig(BaseMasterSlaveServersConfig serverConfig, RedissonProperties.MasterSlaveProperties props) {
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

    private static void configGlobal(Config config, RedissonDatasourceConfig props) {
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


    public static String getPassword(RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        return StrUtil.nullToDefault(properties.getPassword(), redisProperties.getPassword());
    }

    public static Integer getDatabase(RedissonDatasourceConfig properties, RedisProperties redisProperties) {
        return ObjectUtil.defaultIfNull(properties.getDatabase(), redisProperties.getDatabase());
    }
}
