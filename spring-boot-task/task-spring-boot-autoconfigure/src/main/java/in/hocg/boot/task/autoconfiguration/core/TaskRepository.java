package in.hocg.boot.task.autoconfiguration.core;

import in.hocg.boot.task.autoconfiguration.core.dto.ExecTaskDTO;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskInfo;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TaskRepository {
    /**
     * 查询任务类型
     *
     * @param taskType _
     * @return _
     */
    List<ExecTaskDTO> listByTypeAndReady(@NonNull String taskType);

    /**
     * 查询所有准备状态的任务
     *
     * @return
     */
    List<ExecTaskDTO> listByReady();

    /**
     * 创建任务
     *
     * @param taskName
     * @param taskType
     * @param createUser
     * @param params
     * @param delaySecond
     * @param executeNow
     */
    ExecTaskDTO createTask(@NonNull String taskName, @NonNull String taskType, Object params, Long delaySecond);

    default ExecTaskDTO createTask(@NonNull String taskName, @NonNull String taskType, Object params) {
        return this.createTask(taskName, taskType, params, 0L);
    }

    @SneakyThrows(SQLException.class)
    ExecTaskDTO createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount);

    /**
     * 任务过程日志
     *
     * @param isOk
     * @param message
     * @param data
     */
    void log(@NonNull Long taskItemId, String message);

    /**
     * 任务开始
     *
     * @param taskSn
     */
    boolean startTask(@NonNull Long taskItemId);

    /**
     * 任务完成
     * - 记录状态
     * - 记录最终结果
     *
     * @param taskSn
     * @param isOk
     * @param message
     * @param data
     */
    void doneTask(@NonNull Long taskItemId, @NonNull TaskItem.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data);

    /**
     * 获取唯一任务
     *
     * @param taskSn
     * @return
     */
    Optional<ExecTaskDTO> getByTaskItemId(Long taskItemId);

    /**
     * 删除旧任务 <br/>
     * - 状态为 成功 <br/>
     * - 类型为 所有 <br/>
     *
     * @param minusDays 删除多少天前的任务
     * @return 删除的数量
     */
    Integer deleteDays(@NonNull Long minusDays);

    /**
     * 删除旧任务
     *
     * @param minusDays
     * @param eqTypes
     * @param eqStatus
     * @return
     */
    Integer deleteDays(@NonNull Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqStatus);

    /**
     * 重建定时任务
     *
     * @param taskSn
     * @param delaySecond
     * @param maxCount
     */
    void reCreateExecTask(Long taskId, Long delaySecond, Long maxCount);

}
