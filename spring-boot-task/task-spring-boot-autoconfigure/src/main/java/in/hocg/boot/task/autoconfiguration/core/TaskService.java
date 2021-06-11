package in.hocg.boot.task.autoconfiguration.core;

import in.hocg.boot.task.autoconfiguration.TaskAutoConfiguration;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Function;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TaskService {

    /**
     * 异步执行任务
     *
     * @param taskSn   任务编号
     * @param runnable 任务
     * @param <T>      执行结果
     * @return 执行结果
     */
    @Async(TaskAutoConfiguration.EXECUTOR_NAME)
    <T, R> TaskResult<R> runAsync(String taskSn, Function<T, R> runnable);

    /**
     * 同步执行任务
     *
     * @param taskSn   任务编号
     * @param runnable 任务
     * @param <T>      执行结果
     * @return 执行结果
     */
    <T, R> TaskResult<R> runSync(String taskSn, Function<T, R> runnable);

}
