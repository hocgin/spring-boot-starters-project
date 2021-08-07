package in.hocg.boot.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.utils.exception.ToolsException;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class LogUtils {

    public <T> T log(SupplierThrow<T> exec, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        return log(exec, null, onComplete);
    }

    /**
     * @param exec
     * @param onReady
     * @param onComplete logId, logStatus, ResponseBody or Error
     * @param <T>
     * @return
     */
    public <T> T log(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady, ThreeConsumerThrow<Serializable, LogStatus, String> onComplete) {
        LogStatus status = LogStatus.Process;
        String resultBody = null;
        T result;
        Serializable logId = null;
        if (Objects.nonNull(onReady)) {
            try {
                logId = onReady.get();
            } catch (Exception e) {
                log.warn("发生异常", e);
                throw new RuntimeException(e);
            }
        }
        try {
            result = exec.get();
            status = LogStatus.Success;
            if (Objects.isNull(result) || ClassUtil.isBasicType(result.getClass())) {
                resultBody = String.valueOf(result);
            } else {
                resultBody = JSONUtil.toJsonStr(result);
            }
        } catch (Exception e) {
            log.warn("业务执行发生异常: ", e);
            status = LogStatus.Fail;
            resultBody = StrUtil.format("业务执行发生异常: [{}]", e.getMessage());
            throw new ToolsException(e);
        } finally {
            if (Objects.nonNull(onComplete)) {
                try {
                    onComplete.accept(logId, status, resultBody);
                } catch (Exception e) {
                    log.warn("发生异常", e);
                }
            }
            log.info("日志记录执行完成");
        }
        return result;
    }

    public enum LogStatus {
        Process,
        Fail,
        Success,
        ;
    }
}
