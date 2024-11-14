package in.hocg.boot.cache.autoconfiguration.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.cache.autoconfiguration.annotation.RateLimit;
import in.hocg.boot.cache.autoconfiguration.dynamic.DynamicRoutingConnectionFactory;
import in.hocg.boot.cache.autoconfiguration.utils.ElUtils;
import in.hocg.boot.utils.servlet.WebUtils;
import in.hocg.boot.utils.struct.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.*;
import org.springframework.context.annotation.Scope;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author hocgin
 */
@Slf4j
@Scope
@Aspect
@RequiredArgsConstructor
public class RateLimitAspect {

    private final DynamicRoutingConnectionFactory connectionFactory;

    @Pointcut("@annotation(in.hocg.boot.cache.autoconfiguration.annotation.RateLimit)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        // 获取请求
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();
        String limitKey = (StrUtil.replace(request.getRequestURI(), "/", "_") + "_" + WebUtils.getClientIp(request));

        // 获取配置 RRateLimiter 信息
        RRateLimiter rRateLimiter = getRRateLimiter(joinPoint, limitKey);
        Object obj = null;
        if (rRateLimiter.tryAcquire()) {
            obj = joinPoint.proceed();
        } else {
            outRateLimiterMsg(servletRequestAttributes.getResponse(), JSONUtil.toJsonStr(Result.fail("请求被限流")));
        }
        return obj;
    }

    private RRateLimiter getRRateLimiter(ProceedingJoinPoint joinPoint, String limitKey) {
        RateLimit rateLimit = getRateLimitAspect(joinPoint);

        //限流次数
        long count = rateLimit.count();
        //限流时间
        long time = rateLimit.time();

        Object[] arguments = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        String key = ElUtils.parseSpEl(rateLimit.key(), parameterNames, arguments);

        RRateLimiter rRateLimiter = this.getRedissonClient().getRateLimiter(StrUtil.isBlank(key) ? limitKey : key);
        if (rRateLimiter.isExists()) {
            // 读取已经存在配置
            RateLimiterConfig rateLimiterConfig = rRateLimiter.getConfig();

            // 限时时间
            long rateInterval = rateLimiterConfig.getRateInterval();

            // 次数
            long rate = rateLimiterConfig.getRate();
            if (time != rateInterval || rate != count) {

                // 移除配置，重新加载配置
                rRateLimiter.delete();
                rRateLimiter.trySetRate(RateType.OVERALL, count, time, RateIntervalUnit.SECONDS);
            }
        }
        rRateLimiter.trySetRate(RateType.OVERALL, count, time, RateIntervalUnit.SECONDS);
        return rRateLimiter;
    }

    /**
     * 获取脚注配置信息
     *
     * @param joinPoint
     * @return
     */
    private RateLimit getRateLimitAspect(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(RateLimit.class);
    }

    /**
     * 客户端返回数据
     */
    private void outRateLimiterMsg(HttpServletResponse response, String text) {
        try (PrintWriter writer = response.getWriter()) {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/json;charset=utf-8");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
            response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            writer.write(text);
        } catch (Exception e) {
            log.warn("输出日志发生错误: ", e);
        }
    }

    public RedissonClient getRedissonClient() {
        return connectionFactory.getResolvedDefaultDataSource();
    }
}
