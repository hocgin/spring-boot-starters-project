package in.hocg.boot.task.autoconfiguration.core;

import in.hocg.boot.task.autoconfiguration.TaskAutoConfiguration;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TaskBervice {

    /**
     * 异步执行任务
     *
     * @param taskSn   任务编号
     * @param runnable 任务
     * @param <T>      执行结果
     * @return 执行结果
     */
    @Async(TaskAutoConfiguration.EXECUTOR_NAME)
    <T, R> Future<TaskResult<R>> runAsync(Long taskItemId, Function<T, R> runnable, Consumer<TaskItem> failStrategy);

    default <T, R> Future<TaskResult<R>> runAsync(Long taskItemId, Function<T, R> runnable) {
        return runAsync(taskItemId, runnable, FailStrategy.reCreate());
    }

    /**
     * 同步执行任务
     *
     * @param taskItemId 任务项
     * @param runnable   任务
     * @param <T>        执行结果
     * @return 执行结果
     */
    <T, R> TaskResult<R> runSync(Long taskItemId, Function<T, R> runnable, Consumer<TaskItem> failStrategy);

    default <T, R> TaskResult<R> runSync(Long taskItemId, Function<T, R> runnable) {
        return runSync(taskItemId, runnable, FailStrategy.reCreate());
    }

}
