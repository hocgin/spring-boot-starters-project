package in.hocg.boot.task.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class TaskLogger {
    private static final ThreadLocal<Serializable> TASK = new ThreadLocal<>();

    public static void log(CharSequence template, Object... params) {
        log.info(StrUtil.format(template, params));
    }

    public static void asyncLog(CharSequence template, Object... params) {
        TaskLogger.log(template, params);
    }

    protected static void setTaskId(Serializable taskId) {
        TASK.set(taskId);
    }

    protected static void clear() {
        TASK.remove();
    }

}
