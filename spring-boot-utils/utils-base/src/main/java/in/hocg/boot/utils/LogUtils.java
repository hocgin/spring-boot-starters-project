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

    /**
     * 同步记录日志
     *
     * @param exec       执行操作
     * @param onComplete 完成日志状态补充
     * @param <T>
     * @return 执行结果
     */
    public <T> T logSync(SupplierThrow<T> exec, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        return logSync(exec, null, onComplete);
    }

    /**
     * 同步记录日志
     *
     * @param exec       执行操作
     * @param onReady    准备日志
     * @param onComplete 完成日志状态补充
     * @param <T>
     * @return 执行结果
     */
    public <T> T logSync(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        return logAsync(exec, () -> Objects.nonNull(onReady) ? new FutureTask<>(onReady::get) : null, onComplete);
    }

    /**
     * 异步记录日志
     *
     * @param exec       执行操作
     * @param onReady    准备日志
     * @param onComplete 完成日志状态补充
     * @param <T>
     * @return 执行结果
     */
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
                if (onFuture instanceof FutureTask) {
                    ((FutureTask<Serializable>) onFuture).run();
                }
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
                    onComplete.accept(onFuture.get(5, TimeUnit.SECONDS), status, resultBody);
                } catch (Exception e) {
                    log.warn("请求系统日志记录(3)发生异常", e);
                }
            }
            log.debug("日志记录执行完成");
        }
        return result;
    }


    public enum LogStatus {Process, Fail, Success,}
}
