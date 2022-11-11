package in.hocg.boot.cache.autoconfiguration.exception;

import cn.hutool.core.util.StrUtil;

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

    public DistributedLockException(String message, Object... params) {
        this(StrUtil.format(message, params));
    }
}
