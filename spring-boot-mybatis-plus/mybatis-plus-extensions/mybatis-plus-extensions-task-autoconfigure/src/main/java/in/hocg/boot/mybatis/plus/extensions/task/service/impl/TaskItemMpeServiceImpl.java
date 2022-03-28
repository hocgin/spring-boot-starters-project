package in.hocg.boot.mybatis.plus.extensions.task.service.impl;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import in.hocg.boot.mybatis.plus.extensions.task.mapper.TaskItemMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskItemMpeService;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaskItemMpeServiceImpl extends AbstractServiceImpl<TaskItemMpeMapper, TaskItem>
    implements TaskItemMpeService {
    @Override
    public TaskItem createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readyAt = now.plusSeconds(delaySecond);
        Long taskId = taskInfo.getId();

        int idx = taskInfo.getRetryCount() + 1;

        // 判断限制次数
        if (idx > maxCount) {
            return null;
        }
        TaskItem taskItem = new TaskItem()
            .setIdx(idx)
            .setType(taskInfo.getType())
            .setParams(taskInfo.getParams())
            .setStatus(TaskItem.Status.Ready.getCode())
            .setReadyAt(readyAt)
            .setTaskId(taskId);
        saveOrUpdate(taskItem);
        return taskItem;
    }

    @Override
    public List<TaskItem> listByTypeAndStatus(String taskType, String status) {
        return lambdaQuery().eq(Objects.nonNull(taskType), TaskItem::getType, taskType).eq(Objects.nonNull(status), TaskItem::getStatus, status).list();
    }

    @Override
    public boolean start(Long taskItemId) {
        TaskItem updated = new TaskItem().setStatus(TaskItem.Status.Executing.getCode());
        return lambdaUpdate().eq(CommonEntity::getId, taskItemId).eq(TaskItem::getStatus, TaskItem.Status.Ready.getCode())
            .update(updated);
    }

    @Override
    public boolean done(Long taskItemId, TaskItem.DoneStatus doneStatus, Long totalTimeMillis, String message, Object data) {
        TaskItem updated = new TaskItem().setStatus(TaskItem.Status.Done.getCode())
            .setTotalTimeMillis(totalTimeMillis)
            .setDoneMessage(message)
            .setDoneResult(LangUtils.callIfNotNull(data, JSONUtil::toJsonStr).orElse(null))
            .setDoneStatus(doneStatus.getCode());
        updated.setId(taskItemId);
        return updateById(updated);
    }

    @Override
    public Boolean deleteDays(Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqStatus) {
        LocalDateTime preDateTime = LocalDateTime.now().minusDays(minusDays);
        return lambdaUpdate().eq(TaskItem::getStatus, TaskItem.Status.Done.getCodeStr())
            .lt(CommonEntity::getCreatedAt, preDateTime)
            .in(Objects.nonNull(eqTypes), TaskItem::getType, eqTypes)
            .in(Objects.nonNull(eqStatus), TaskItem::getDoneStatus, eqStatus)
            .remove();
    }

    @Override
    public Boolean doneExpiredDays(Long minusDays, List<String> types) {
        LocalDateTime preDateTime = LocalDateTime.now().minusDays(minusDays);
        TaskItem update = new TaskItem();
        update.setStatus(TaskItem.Status.Done.getCode());
        update.setDoneMessage("任务超时, 自动关闭");
        update.setDoneStatus(TaskItem.DoneStatus.Expired.getCode());
        return lambdaUpdate().eq(TaskItem::getStatus, TaskItem.Status.Executing.getCodeStr())
            .lt(CommonEntity::getCreatedAt, preDateTime)
            .in(Objects.nonNull(types), TaskItem::getType, types)
            .update(update);
    }
}
