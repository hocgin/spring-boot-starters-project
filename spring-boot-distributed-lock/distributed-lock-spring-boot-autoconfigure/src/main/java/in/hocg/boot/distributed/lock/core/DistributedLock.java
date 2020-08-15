package in.hocg.boot.distributed.lock.core;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface DistributedLock {

    void removeLock(String key);

    boolean getLock(String key);
}
