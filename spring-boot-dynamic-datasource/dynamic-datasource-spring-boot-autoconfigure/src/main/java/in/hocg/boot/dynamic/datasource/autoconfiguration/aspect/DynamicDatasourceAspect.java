package in.hocg.boot.dynamic.datasource.autoconfiguration.aspect;

import in.hocg.boot.dynamic.datasource.autoconfiguration.core.DynamicDatasource;
import in.hocg.boot.dynamic.datasource.autoconfiguration.core.DynamicDatasourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * @author hocgin
 * @date 2019/6/25
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE)
public class DynamicDatasourceAspect {

    @Around("@annotation(annotation)")
    public Object aspect(ProceedingJoinPoint point, DynamicDatasource annotation) throws Throwable {
        String datasourceName = annotation.name();
        if (!DynamicDatasourceHolder.isExist(datasourceName)) {
            log.warn("切换数据源发生错误, 数据源 {} 不存在", datasourceName);
        } else {
            DynamicDatasourceHolder.setDatasource(datasourceName);
            log.debug("切换数据源, 当前数据源为 {}", datasourceName);
        }
        try {
            return point.proceed();
        } finally {
            DynamicDatasourceHolder.clear();
        }
    }
}
