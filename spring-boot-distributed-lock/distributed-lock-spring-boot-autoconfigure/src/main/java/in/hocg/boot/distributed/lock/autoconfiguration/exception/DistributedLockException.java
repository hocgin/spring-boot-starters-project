package in.hocg.boot.distributed.lock.autoconfiguration.exception;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class DistributedLockException extends RuntimeException {
    public DistributedLockException(String message) {
        super(message);
    }
}
