package in.hocg.boot.mybatis.plus.extensions.task.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;

import java.util.List;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TaskItemMpeService extends AbstractService<TaskItem> {
    TaskItem createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount);

    List<TaskItem> listByTypeAndStatus(String taskType, String status);

    boolean start(Long taskItemId);

    boolean done(Long taskItemId, TaskItem.DoneStatus doneStatus, Long totalTimeMillis, String message, Object data);

    Boolean deleteDays(Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqStatus);

    Boolean doneExpiredDays(Long minusDays, List<String> types);
}
