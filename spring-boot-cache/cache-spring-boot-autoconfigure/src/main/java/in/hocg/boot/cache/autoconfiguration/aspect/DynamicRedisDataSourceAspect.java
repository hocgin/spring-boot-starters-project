package in.hocg.boot.cache.autoconfiguration.aspect;

import in.hocg.boot.cache.autoconfiguration.annotation.RDS;
import in.hocg.boot.cache.autoconfiguration.dynamic.DynamicDatasourceHolder;
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
public class DynamicRedisDataSourceAspect {

    @Around("@annotation(annotation)")
    public Object aspect(ProceedingJoinPoint point, RDS annotation) throws Throwable {
        String datasource = annotation.value();
        DynamicDatasourceHolder.push(datasource);
        try {
            return point.proceed();
        } finally {
            DynamicDatasourceHolder.poll();
        }
    }
}
