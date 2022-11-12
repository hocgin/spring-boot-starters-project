package in.hocg.boot.cache.autoconfiguration.aspect;

import cn.hutool.core.util.ArrayUtil;
import in.hocg.boot.cache.autoconfiguration.annotation.DistributeLock;
import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import in.hocg.boot.cache.autoconfiguration.exception.DistributedLockException;
import in.hocg.boot.cache.autoconfiguration.lock.DistributedLock;
import in.hocg.boot.cache.autoconfiguration.properties.RedissonProperties;
import in.hocg.boot.cache.autoconfiguration.utils.ElUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author hocgin
 */
@Slf4j
@Aspect
@Order(-10)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class DistributeLockAspect {
    public static final String LOCK_PREFIX = "redisson:lock:";

    private final RedissonProperties properties;

    private final DistributedLock distributedLock;

    @Pointcut("@annotation(lock)")
    public void aspect(DistributeLock lock) {
    }


    @Around(value = "aspect(lock)", argNames = "joinPoint,lock")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, DistributeLock lock) throws Throwable {
        String[] keys = lock.keys();
        if (keys.length == 0) {
            throw new DistributedLockException("keys 不能为空");
        }

        LockType lockType = getLockType(lock, keys, properties);
        Long tryTimeout = getTryWaitTimeout(lock, properties);
        Long lockTimeout = getLockTimeout(lock, properties);
        if (!lockType.equals(LockType.Multiple) && !lockType.equals(LockType.RedLock) && keys.length > 1) {
            throw new DistributedLockException("参数有多个, 锁模式为 [{}] 无法锁定", lockType);
        }
        log.debug("锁模式[{}],等待锁定时间[{}]秒,锁定最长时间[{}]秒", lockType, tryTimeout / 1000, lockTimeout / 1000);
        RLock rLock = distributedLock.getLock(getLockKeys(joinPoint, lock), lockType);
        if (Objects.isNull(rLock)) {
            throw new DistributedLockException("获取锁失败");
        }
        boolean locked = false;
        try {
            if (tryTimeout == -1) {
                locked = true;
                rLock.lock(lockTimeout, TimeUnit.MILLISECONDS);
            } else {
                locked = rLock.tryLock(tryTimeout, lockTimeout, TimeUnit.MILLISECONDS);
            }
            if (locked) {
                return joinPoint.proceed();
            } else {
                throw new DistributedLockException("获取锁失败");
            }
        } finally {
            if (locked) {
                rLock.unlock();
            }
        }
    }

    private Long getLockTimeout(DistributeLock lock, RedissonProperties properties) {
        long timeout = lock.lockTimeout();
        if (timeout == 0) {
            timeout = properties.getLockWatchdogTimeout();
        }
        return timeout;
    }

    private Long getTryWaitTimeout(DistributeLock lock, RedissonProperties properties) {
        long timeout = lock.tryWaitTimeout();
        if (timeout == 0) {
            timeout = properties.getTryWaitTimeout();
        }
        return timeout;
    }

    private LockType getLockType(DistributeLock lock, String[] keys, RedissonProperties properties) {
        LockType lockType = lock.lockType();
        if (lockType.equals(LockType.Auto)) {
            lockType = properties.getDefaultLockType();
            if (keys.length > 1) {
                lockType = LockType.RedLock;
            } else if (Objects.isNull(lockType)) {
                lockType = LockType.Reentrant;
            }
        }
        return lockType;
    }


    /**
     * 获取锁标识
     *
     * @param point 点
     * @param lock  锁
     */
    private String[] getLockKeys(ProceedingJoinPoint point, DistributeLock lock) {
        String[] keys = lock.keys();
        if (ArrayUtil.isEmpty(keys)) {
            throw new UnsupportedOperationException("@DistributeLock.keys 不能为空");
        }
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(((MethodSignature) point.getSignature()).getMethod());
        Object[] args = point.getArgs();
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            result.addAll(getValueBySpEl(key, parameterNames, args, lock.keyConstant()));
        }
        return result.toArray(String[]::new);
    }

    public List<String> getValueBySpEl(String key, String[] parameterNames, Object[] values, String keyConstant) {
        List<String> keys = new ArrayList<>();
        if (!key.contains("#")) {
            String s = LOCK_PREFIX + key + keyConstant;
            log.debug("没有使用 SpEL 表达式[{}]", s);
            keys.add(s);
            return keys;
        }
        Object value = ElUtils.parseSpEl(key, parameterNames, values);
        if (value != null) {
            if (value instanceof List) {
                for (Object o : (List<?>) value) {
                    keys.add(LOCK_PREFIX + o.toString() + keyConstant);
                }
            } else if (value.getClass().isArray()) {
                Object[] obj = (Object[]) value;
                for (Object o : obj) {
                    keys.add(LOCK_PREFIX + o.toString() + keyConstant);
                }
            } else {
                keys.add(LOCK_PREFIX + value + keyConstant);
            }
        }
        log.debug("SpEL 表达式 key=[{}], value=[{}]", key, keys);
        return keys;
    }
}
