package in.hocg.boot.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class LogUtils {

    public <T> T logSync(SupplierThrow<T> exec, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        return logSync(exec, null, onComplete);
    }

    /**
     * @param exec
     * @param onReady
     * @param onComplete logId, logStatus, ResponseBody or Error
     * @param <T>
     * @return
     */
    public <T> T logSync(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        return logAsync(exec, () -> Objects.nonNull(onReady) ? new FutureTask<>(onReady::get) : null, onComplete);
    }

    public <T> T logAsync(SupplierThrow<T> exec, SupplierThrow<Future<Serializable>> onReady,
                          ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        LogStatus status = LogStatus.Process;
        String resultBody = null;
        T result;

        // 1. 准备日志
        Future<Serializable> onFuture = null;
        if (Objects.nonNull(onReady)) {
            try {
                onFuture = onReady.get();
            } catch (Exception e) {
                log.warn("请求系统日志记录(1)发生异常", e);
                throw new RuntimeException(e);
            }
        }

        // 2. 执行业务
        try {
            result = exec.get();
            status = LogStatus.Success;
            if (Objects.isNull(result) || ClassUtil.isBasicType(result.getClass())) {
                resultBody = String.valueOf(result);
            } else {
                resultBody = JSONUtil.toJsonStr(result);
            }
        } catch (Exception e) {
            log.warn("请求系统业务执行(2)发生异常: ", e);
            status = LogStatus.Fail;
            resultBody = StrUtil.format("业务执行发生异常: [{}]", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // 3. 完成日志
            if (Objects.nonNull(onFuture) && Objects.nonNull(onComplete)) {
                try {
                    Serializable logId = onFuture.get(2, TimeUnit.SECONDS);
                    onComplete.accept(logId, status, resultBody);
                } catch (Exception e) {
                    log.warn("请求系统日志记录(3)发生异常", e);
                }
            }
            log.debug("日志记录执行完成");
        }
        return result;
    }


    public enum LogStatus {
        Process, Fail, Success;
    }
}