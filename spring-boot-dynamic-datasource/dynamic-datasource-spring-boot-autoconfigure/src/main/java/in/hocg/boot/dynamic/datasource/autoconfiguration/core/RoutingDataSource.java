package in.hocg.boot.dynamic.datasource.autoconfiguration.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * @author hocgin
 * @date 2019/6/25
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 根据规则，获取数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDatasourceHolder.getDataSource();
    }
}
