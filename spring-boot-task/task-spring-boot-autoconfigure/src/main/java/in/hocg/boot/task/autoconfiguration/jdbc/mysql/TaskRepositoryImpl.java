package in.hocg.boot.task.autoconfiguration.jdbc.mysql;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.dto.TaskDTO;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskInfo;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItemLog;
import in.hocg.boot.utils.LambdaUtils;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.utils.context.UserContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final DataSource dataSource;

    @Override
    @SneakyThrows(SQLException.class)
    public List<TaskItem> listByTypeAndReady(@NonNull String taskType) {
        Entity where = Entity.create(TaskItem.TABLE_NAME)
            .set(LambdaUtils.getColumnName(TaskItem::getType), taskType)
            .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Ready.getCode());
        return Db.use(dataSource).find(where).stream().map(TaskItem::as).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public List<TaskItem> listByReady() {
        Entity where = Entity.create(TaskItem.TABLE_NAME)
            .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Ready.getCode());
        return Db.use(dataSource).find(where).stream().map(TaskItem::as).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public TaskItem createTask(@NonNull String taskName, @NonNull String taskType, Object params, Long delaySecond) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = UserContextHolder.getUserId();

        String taskSn = IdUtil.objectId();
        String paramsStr = LangUtils.callIfNotNull(params, JSONUtil::toJsonStr).orElse(null);

        TaskInfo taskInfo = new TaskInfo()
            .setTitle(taskName)
            .setParams(paramsStr)
            .setType(taskType)
            .setRetryCount(0)
            .setCreator(userId)
            .setCreatedAt(now)
            .setTaskSn(taskSn);
        Long taskId = Db.use(dataSource).insertForGeneratedKey(taskInfo.asEntity());
        taskInfo.setId(taskId);
        return createExecTaskByTask(taskInfo, LangUtils.getOrDefault(delaySecond, 10L), 5L);
    }

    @Override
    public Optional<TaskDTO> getLastTaskId(Long taskId) {
        return getByTaskId(taskId).flatMap(taskInfo -> {
            Integer retryCount = taskInfo.getRetryCount();
            if (retryCount == 0) {
                return Optional.empty();
            }
            Optional<TaskItem> lastTaskItem = getByTaskIdAndIdx(taskId, taskInfo.getRetryCount());
            if (!lastTaskItem.isPresent()) {
                return Optional.empty();
            }
            TaskItem taskItem = lastTaskItem.get();
            return Optional.of(TaskDTO.as(taskInfo, taskItem));
        });
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Optional<TaskItem> getByTaskIdAndIdx(Long taskId, Integer idx) {
        Entity where = new TaskItem().setTaskId(taskId).setIdx(idx).setStatus(TaskItem.Status.Done.getCode()).asEntity();
        Entity entity = Db.use(dataSource).get(where);
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        return Optional.of(TaskItem.as(entity));
    }


    @Override
    @SneakyThrows(SQLException.class)
    public TaskItem createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readyAt = now.plusSeconds(delaySecond);
        Long userId = UserContextHolder.getUserId();
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
            .setCreator(userId)
            .setCreatedAt(now)
            .setReadyAt(readyAt)
            .setTaskId(taskId);
        Entity entity = taskItem.asEntity();
        Long taskItemId = Db.use(dataSource).insertForGeneratedKey(entity);
        taskItem.setId(taskItemId);
        return taskItem;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void log(@NonNull Long taskItemId, String message) {
        LocalDateTime now = LocalDateTime.now();

        TaskItemLog taskLog = new TaskItemLog()
            .setContent(message)
            .setTaskItemId(taskItemId)
            .setCreatedAt(now)
            .setCreator(UserContextHolder.getUserId());

        Db.use(dataSource).insert(taskLog.asEntity());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public boolean startTask(@NonNull Long taskItemId) {
        LocalDateTime now = LocalDateTime.now();
        Entity where = Entity.create(TaskItem.TABLE_NAME)
            .set(LambdaUtils.getColumnName(TaskItem::getId), taskItemId)
            .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Ready.getCode());
        Entity update = Entity.create(TaskItem.TABLE_NAME)
            .set(LambdaUtils.getColumnName(TaskItem::getStartAt), now)
            .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Executing.getCode());
        return Db.use(dataSource).update(update, where) > 0;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void doneTask(@NonNull Long taskItemId, @NonNull TaskItem.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data) {
        Db.use(dataSource).tx(db -> {
            Boolean isUpdate = getByTaskItemId(taskItemId).map(TaskItem::getTaskId).flatMap(this::getByTaskId).map(this::incrRetryCount).orElse(false);
            if (isUpdate) {
                LocalDateTime now = LocalDateTime.now();
                Entity where = Entity.create(TaskItem.TABLE_NAME)
                    .set(LambdaUtils.getColumnName(TaskItem::getId), taskItemId)
                    .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Executing.getCode());
                Entity update = Entity.create(TaskItem.TABLE_NAME)
                    .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneMessage), message)
                    .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneResult), LangUtils.callIfNotNull(data, JSONUtil::toJsonStr).orElse(null))
                    .set(LambdaUtils.getColumnName(TaskItem::getDoneStatus), doneStatus.getCode())
                    .set(LambdaUtils.getColumnName(TaskItem::getDoneAt), now)
                    .set(LambdaUtils.getColumnName(TaskItem::getTotalTimeMillis), totalTimeMillis)
                    .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Done.getCode());
                db.update(update, where);
            }
        });
    }

    @SneakyThrows(SQLException.class)
    public boolean incrRetryCount(TaskInfo taskInfo) {
        Integer whereRetryCount = taskInfo.getRetryCount();
        int targetRetryCount = whereRetryCount + 1;
        Entity update = new TaskInfo()
            .setRetryCount(targetRetryCount)
            .setLastUpdater(UserContextHolder.getUserId())
            .setLastUpdatedAt(LocalDateTime.now())
            .asEntity();
        Entity where = new TaskInfo().setId(taskInfo.getId()).setRetryCount(whereRetryCount).asEntity();
        return Db.use(dataSource).update(update, where) > 0;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Optional<TaskItem> getByTaskItemId(@NonNull Long taskItemId) {
        Entity entity = Db.use(dataSource).get(Entity.create(TaskItem.TABLE_NAME).set(LambdaUtils.getColumnName(TaskItem::getId), taskItemId));
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        return Optional.ofNullable(TaskItem.as(entity));
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Optional<TaskInfo> getByTaskId(Long taskId) {
        Entity where = new TaskInfo().setId(taskId)
            .asEntity();
        Entity entity = Db.use(dataSource).get(where);
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        return Optional.of(TaskInfo.as(entity));
    }

    @Override
    public Integer deleteDays(@NonNull Long minusDays) {
        return this.deleteDays(minusDays, null, Lists.newArrayList(TaskItem.DoneStatus.Success));
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Integer deleteDays(@NonNull Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqDoneStatus) {
        LocalDateTime preDateTime = LocalDateTime.now().minusDays(minusDays);
        Entity where = Entity.create(TaskItem.TABLE_NAME)
            .set(LambdaUtils.getColumnName(TaskItem::getStatus), TaskItem.Status.Done.getCode())
            .set(LambdaUtils.getColumnName(TaskItem::getCreatedAt), StrUtil.format("< {}", DateUtil.formatLocalDateTime(preDateTime)));
        if (Objects.nonNull(eqTypes)) {
            where = where.set(LambdaUtils.getColumnName(TaskItem::getType), StrUtil.format("in {}", ArrayUtil.join(eqTypes.toArray(), ",")));
        }
        if (Objects.nonNull(eqDoneStatus)) {
            where = where.set(LambdaUtils.getColumnName(TaskItem::getDoneStatus), StrUtil.format("in {}", ArrayUtil.join(eqDoneStatus.stream().map(TaskItem.DoneStatus::getCode).toArray(), ",")));
        }
        return Db.use(dataSource).del(where);
    }

    @Override
    public void reCreateExecTask(Long taskId, Long delaySecond, Long maxCount) {
        Optional<TaskInfo> taskOpt = getByTaskId(taskId);
        if (!taskOpt.isPresent()) {
            return;
        }
        TaskInfo taskInfo = taskOpt.get();
        createExecTaskByTask(taskInfo, delaySecond, maxCount);
    }


}
