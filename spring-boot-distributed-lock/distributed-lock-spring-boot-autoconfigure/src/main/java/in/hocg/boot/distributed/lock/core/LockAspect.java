package in.hocg.boot.distributed.lock.core;

import in.hocg.boot.distributed.lock.annotation.LockKey;
import in.hocg.boot.distributed.lock.annotation.UseLock;
import in.hocg.boot.distributed.lock.exception.DistributedLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2019-09-29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LockAspect {

    private final DistributedLock lock;

    @Pointcut("@annotation(in.hocg.boot.distributed.lock.annotation.LockKey)")
    public void aspect() {
    }


    @Around("aspect()")
    public Object doAspect(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getTarget();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        UseLock annotation = method.getAnnotation(UseLock.class);
        int tryNumber = annotation.tryNumber();
        long tryInterval = annotation.tryInterval();
        TimeUnit timeUnit = annotation.expireTimeUnit();
        long timeout = annotation.expireTime();

        // 根据类型获取锁标识
        String key = getLockKey(point, method, annotation);

        log.debug("正在申请锁, 锁标识为: {}", key);
        if (!tryGetLock(key, tryNumber, tryInterval, timeout, timeUnit)) {
            log.debug("申请锁失败, 锁标识为: {}", key);
            throw new DistributedLockException(annotation.errorMessage());
        }
        Object ret;
        try {
            ret = point.proceed();
        } finally {
            log.debug("正在释放锁, 锁标识为: {}", key);
            lock.release(key);
        }
        return ret;
    }

    /**
     * 尝试多次获取锁
     *
     * @param key       锁
     * @param tryNumber 尝试次数
     * @return
     */
    private boolean tryGetLock(String key, int tryNumber, long tryInterval, long timeout, TimeUnit timeUnit) throws InterruptedException {
        for (int i = 0; i < tryNumber; i++) {
            if (lock.acquire(key, timeout, timeUnit)) {
                return true;
            }
            Thread.sleep(tryInterval);
        }
        return false;
    }

    /**
     * 获取锁标识
     *
     * @param point
     * @param method
     * @param annotation
     * @return
     */
    private String getLockKey(ProceedingJoinPoint point, Method method, UseLock annotation) {
        String prefix = annotation.prefix();
        String key;
        switch (annotation.keyType()) {
            case Parameter: {
                String lockKey = null;
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    boolean hasKeyParam = parameter.isAnnotationPresent(LockKey.class);
                    if (hasKeyParam) {
                        Object arg = point.getArgs()[i];
                        if (arg instanceof String || arg instanceof Integer) {
                            lockKey = prefix + arg;
                            break;
                        }
                    }
                }
                if (Objects.nonNull(lockKey)) {
                    key = lockKey;
                    break;
                }
                throw new IllegalArgumentException("未找到 Redis Key, 请在函数参数上指定锁的标识，使用 @" + LockKey.class.getName());
            }
            case MethodName: {
                key = method.getName();
                break;
            }
            case Key:
            default: {
                key = annotation.key();
            }
        }
        return prefix + "#" + key;
    }
}
