package in.hocg.boot.task.autoconfiguration.core;

import in.hocg.boot.task.autoconfiguration.jdbc.TableTask;
import lombok.NonNull;

import java.io.Serializable;
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
    List<TaskInfo> listByTypeAndReady(@NonNull Serializable taskType);

    /**
     * 查询所有准备状态的任务
     *
     * @return
     */
    List<TaskInfo> listByReady();

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
    TaskInfo createTask(@NonNull Serializable taskName, @NonNull Serializable taskType, @NonNull Serializable createUser,
                        Object params, @NonNull Long delaySecond);


    default TaskInfo createTask(@NonNull Serializable taskName, @NonNull Serializable taskType, @NonNull Serializable createUser, Object params) {
        return this.createTask(taskName, taskType, createUser, params, 0L);
    }

    /**
     * 任务过程日志
     *
     * @param isOk
     * @param message
     * @param data
     */
    void execTaskLog(@NonNull Serializable taskSn, String message);

    /**
     * 任务开始
     *
     * @param taskSn
     */
    boolean startTask(@NonNull Serializable taskSn);

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
    boolean doneTask(@NonNull Serializable taskSn, @NonNull TableTask.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data);

    /**
     * 获取唯一任务
     *
     * @param taskSn
     * @return
     */
    Optional<TaskInfo> getByTaskSn(Serializable taskSn);

}
