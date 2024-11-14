package in.hocg.boot.cache.autoconfiguration.lock;

import in.hocg.boot.cache.autoconfiguration.dynamic.DynamicRoutingConnectionFactory;
import in.hocg.boot.cache.autoconfiguration.enums.LockType;
import in.hocg.boot.cache.autoconfiguration.exception.DistributedLockException;
import in.hocg.boot.utils.function.SupplierThrow;
import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author hocgin
 */
@RequiredArgsConstructor
public class RedissonDistributedLock implements DistributedLock {

    private final DynamicRoutingConnectionFactory connectionFactory;

    @Override
    public boolean tryLock(String[] lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        return this.getLock(lockName, lockType).tryLock(waitTime, leaseTime, timeUnit);
    }

    @Override
    public void unLock(String[] lockName, LockType lockType) {
        this.getLock(lockName, lockType).unlock();
    }

    @Override
    public <T> T lock(SupplierThrow<T> supplier, String[] lockName, LockType lockType, long waitTime, long leaseTime, TimeUnit timeUnit) {
        try {
            boolean result = this.tryLock(lockName, lockType, waitTime, leaseTime, timeUnit);
            if (result) {
                return supplier.get();
            }
        } catch (Exception e) {
            throw new DistributedLockException(e.getMessage());
        } finally {
            this.unLock(lockName, lockType);
        }
        return null;
    }


    @Override
    public RLock getLock(String[] lockName, LockType lockType) {
        RedissonClient redissonClient = getRedissonClient();
        RLock lock;
        switch (lockType) {
            case Fair:
                lock = redissonClient.getFairLock(lockName[0]);
                break;
            case Reentrant:
                lock = redissonClient.getLock(lockName[0]);
                break;
            case Read:
                RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName[0]);
                lock = readWriteLock.readLock();
                break;
            case Write:
                readWriteLock = redissonClient.getReadWriteLock(lockName[0]);
                lock = readWriteLock.writeLock();
                break;
            case RedLock:
                RLock[] rLocks = new RLock[lockName.length];
                for (int i = 0; i < rLocks.length; i++) {
                    rLocks[i] = redissonClient.getLock(lockName[i]);
                }
                lock = new RedissonRedLock(rLocks);
                break;
            case Multiple:
                rLocks = new RLock[lockName.length];
                for (int i = 0; i < rLocks.length; i++) {
                    rLocks[i] = redissonClient.getLock(lockName[i]);
                }
                lock = new RedissonMultiLock(rLocks);
                break;
            default:
                throw new DistributedLockException("找不到对应的类型: {}", lockType);
        }
        return lock;
    }

    public RedissonClient getRedissonClient() {
        return connectionFactory.getResolvedDefaultDataSource();
    }
}
