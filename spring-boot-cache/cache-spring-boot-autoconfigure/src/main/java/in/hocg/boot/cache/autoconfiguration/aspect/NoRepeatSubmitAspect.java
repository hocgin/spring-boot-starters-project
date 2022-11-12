package in.hocg.boot.cache.autoconfiguration.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cache.autoconfiguration.annotation.NoRepeatSubmit;
import in.hocg.boot.cache.autoconfiguration.repository.CacheRepository;
import in.hocg.boot.utils.servlet.WebUtils;
import in.hocg.boot.utils.struct.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author hocgin
 */
@Slf4j
@Aspect
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NoRepeatSubmitAspect {
    private final CacheRepository cacheRepository;

    @Around("@annotation(in.hocg.boot.cache.autoconfiguration.annotation.NoRepeatSubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // ip
        String ip = WebUtils.getClientIp(request);

        // 获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 目标类、方法
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String ipKey = String.format("%s#%s", className, methodName);
        int hashCode = Math.abs(ipKey.hashCode());

        // 重点是此 Key 生成规则
        String key = String.format("%s_%d", ip, hashCode);

        NoRepeatSubmit noRepeatSubmit = method.getAnnotation(NoRepeatSubmit.class);
        if (StrUtil.isNotEmpty(noRepeatSubmit.key())) {
            key = noRepeatSubmit.key();
        }
        long timeout = noRepeatSubmit.timeout();
        if (timeout < 0L) {
            timeout = 5L;
        }

        boolean result = cacheRepository.setNxAndExpire(key, IdUtil.randomUUID(), timeout * 1000);
        if (!result) {
            return Result.fail("请勿重复提交");
        }
        return point.proceed();
    }
}
