package in.hocg.boot.mybatis.plus.extensions.task;

import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.extensions.task.autoconfiguration.TaskMybatisPlusExtAutoConfiguration;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import in.hocg.boot.mybatis.plus.extensions.task.pojo.dto.TaskInfoItemDTO;
import in.hocg.boot.mybatis.plus.extensions.task.support.FailStrategy;
import in.hocg.boot.mybatis.plus.extensions.task.support.TaskResult;
import lombok.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TaskMpeService {
    /**
     * 创建任务
     *
     * @param taskName
     * @param taskType
     * @param params
     * @param delaySecond
     */
    TaskItem createTask(@NonNull String taskName, @NonNull String taskType, Object params, Long delaySecond);

    default TaskItem createTask(@NonNull String taskName, @NonNull String taskType, Object params) {
        return this.createTask(taskName, taskType, params, 0L);
    }

    Optional<TaskInfoItemDTO> getLastTaskId(Long taskId);

    Optional<TaskItem> getByTaskIdAndIdx(Long taskId, Integer idx);

    /**
     * 任务过程日志
     *
     * @param taskItemId
     * @param message
     */
    void log(@NonNull Long taskItemId, String message);

    /**
     * 任务开始
     *
     * @param taskItemId
     */
    boolean start(@NonNull Long taskItemId);

    /**
     * 任务完成
     * - 记录状态
     * - 记录最终结果
     *
     * @param message
     * @param data
     */
    void done(@NonNull Long taskItemId, @NonNull TaskItem.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data);

    /**
     * 获取唯一任务
     *
     * @param taskItemId
     * @return
     */
    Optional<TaskItem> getByTaskItemId(Long taskItemId);

    Optional<TaskInfo> getByTaskId(Long taskId);

    void reCreateExecTask(Long taskId, Long delaySecond, Long maxCount);

    TaskItem createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount);

    /**
     * 删除旧任务 <br/>
     * - 状态为 成功 <br/>
     * - 类型为 所有 <br/>
     *
     * @param minusDays 删除多少天前的任务
     * @return 删除的数量
     */
    default Boolean deleteDays(@NonNull Long minusDays) {
        return this.deleteDays(minusDays, null, Lists.newArrayList(TaskItem.DoneStatus.Success));
    }

    /**
     * 删除旧任务
     *
     * @param minusDays
     * @param eqTypes
     * @param eqStatus
     * @return
     */
    Boolean deleteDays(@NonNull Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqStatus);

    /**
     * 标记为过期时间
     *
     * @param minusDays
     * @param types
     * @return
     */
    Boolean doneExpiredDays(@NonNull Long minusDays, List<String> types);

    default Boolean doneExpiredDays(@NonNull Long minusDays) {
        return this.doneExpiredDays(minusDays, null);
    }


    /**
     * 查询任务类型
     *
     * @param taskType
     * @return
     */
    List<TaskItem> listByTypeAndReady(@NonNull String taskType);

    /**
     * 查询所有准备状态的任务
     *
     * @return
     */
    List<TaskItem> listByReady();


    /**
     * 异步执行任务
     *
     * @param taskItemId 任务编号
     * @param runnable   任务
     * @param <T>        执行结果
     * @return 执行结果
     */
    @Async(TaskMybatisPlusExtAutoConfiguration.EXECUTOR_NAME)
    default <T, R> Future<TaskResult<R>> runAsync(Long taskItemId, Function<T, R> runnable, Consumer<TaskItem> failStrategy) {
        return AsyncResult.forValue(this.runSync(taskItemId, runnable, failStrategy));
    }

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
