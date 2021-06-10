package in.hocg.boot.dynamic.datasource.autoconfiguration.core;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * 数据源绑定到当前线程
 *
 * @author hocgin
 * @date 2019/6/25
 */
@Slf4j
public class DynamicDatasourceHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDERS = new ThreadLocal<>();

    protected static Set<Object> ALL_DATA_SOURCE = Collections.emptySet();

    public static void setDatasource(String datasource) {
        CONTEXT_HOLDERS.set(datasource);
    }

    public static String getDataSource() {
        return CONTEXT_HOLDERS.get();
    }

    public static void clear() {
        CONTEXT_HOLDERS.remove();
    }

    public static void setDatasourceNames(Set<Object> datasourceNames) {
        ALL_DATA_SOURCE = datasourceNames;
    }

    public static boolean isExist(String datasource) {
        return ALL_DATA_SOURCE.contains(datasource);
    }
}
