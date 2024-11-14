package in.hocg.boot.cache.autoconfiguration.dynamic;

import org.redisson.api.RedissonClient;

import javax.annotation.Nullable;
import java.util.Map;

public class DynamicRoutingConnectionFactory extends AbstractRoutingConnectionFactory {
    public DynamicRoutingConnectionFactory(Map<String, RedissonClient> targetDataSources) {
        super(targetDataSources);
    }

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDatasourceHolder.peek();
    }
}
