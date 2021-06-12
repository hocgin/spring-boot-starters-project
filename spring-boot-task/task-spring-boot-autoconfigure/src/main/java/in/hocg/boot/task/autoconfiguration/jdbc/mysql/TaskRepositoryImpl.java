package in.hocg.boot.task.autoconfiguration.jdbc.mysql;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.task.autoconfiguration.core.TaskInfo;
import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.jdbc.TableTask;
import in.hocg.boot.task.autoconfiguration.jdbc.TableTaskLog;
import in.hocg.boot.utils.LangUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.Serializable;
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
    public List<TaskInfo> listByType(@NonNull Serializable taskType) {
        return Db.use(dataSource).find(Entity.create(TableTask.TABLE_NAME).set(TableTask.FIELD_TYPE, taskType))
            .stream().map(entity -> new TaskInfo()
                .setId(entity.getLong(TableTask.FIELD_ID))
                .setTaskSn(entity.getStr(TableTask.FIELD_TASK_SN))
                .setReadyAt(DateUtil.toLocalDateTime(entity.getDate(TableTask.FIELD_READY_AT)))
                .setParams(entity.getStr(TableTask.FIELD_PARAMS)))
            .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public TaskInfo createTask(@NonNull Serializable taskName, @NonNull Serializable taskType, @NonNull Serializable createUser, Object params, @NonNull Long delaySecond, boolean executeNow) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readyAt = now.plusSeconds(delaySecond);

        TaskInfo result = new TaskInfo();
        result.setTaskSn(IdUtil.objectId());
        result.setParams(LangUtils.callIfNotNull(params, JSONUtil::toJsonStr).orElse(null));
        result.setReadyAt(readyAt);

        Long id = Db.use(dataSource).insertForGeneratedKey(
            Entity.create(TableTask.TABLE_NAME)
                .setIgnoreNull(TableTask.FIELD_PARAMS, result.getParams())
                .set(TableTask.FIELD_TASK_SN, result.getTaskSn())
                .set(TableTask.FIELD_TITLE, taskName)
                .set(TableTask.FIELD_TYPE, taskType)
                .set(TableTask.FIELD_CREATOR, createUser)
                .set(TableTask.FIELD_CREATED_AT, now)
                .set(TableTask.FIELD_READY_AT, readyAt)
        );
        return result.setId(id);
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void execTaskLog(@NonNull Serializable taskSn, String message) {
        TaskInfo task = getByTaskSn(taskSn).orElseThrow(() -> new UnsupportedOperationException(StrUtil.format("未找到任务, 任务编号[{}]错误", taskSn)));
        LocalDateTime now = LocalDateTime.now();
        Db.use(dataSource).insert(
            Entity.create(TableTaskLog.TABLE_NAME)
                .setIgnoreNull(TableTaskLog.FIELD_CONTENT, message)
                .set(TableTaskLog.FIELD_TASK_ID, task.getId())
                .set(TableTaskLog.FIELD_CREATED_AT, now)
        );
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void startTask(@NonNull Serializable taskSn) {
        LocalDateTime now = LocalDateTime.now();
        Entity where = Entity.create(TableTask.TABLE_NAME).set(TableTask.FIELD_TASK_SN, taskSn);
        Entity update = Entity.create(TableTask.TABLE_NAME)
            .set(TableTask.FIELD_START_AT, now)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Executing.getCode());
        Db.use(dataSource).update(update, where);
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void doneTask(@NonNull Serializable taskSn, @NonNull TableTask.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data) {
        LocalDateTime now = LocalDateTime.now();
        Entity where = Entity.create(TableTask.TABLE_NAME).set(TableTask.FIELD_TASK_SN, taskSn);
        Entity update = Entity.create(TableTask.TABLE_NAME)
            .setIgnoreNull(TableTask.FIELD_DONE_MESSAGE, message)
            .setIgnoreNull(TableTask.FIELD_DONE_RESULT, LangUtils.callIfNotNull(data, JSONUtil::toJsonStr).orElse(null))
            .set(TableTask.FIELD_DONE_STATUS, doneStatus.getCode())
            .set(TableTask.FIELD_DONE_AT, now)
            .set(TableTask.FIELD_TOTAL_TIME_MILLIS, totalTimeMillis)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Done.getCode());
        Db.use(dataSource).update(update, where);
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Optional<TaskInfo> getByTaskSn(@NonNull Serializable taskSn) {
        Entity entity = Db.use(dataSource).get(Entity.create(TableTask.TABLE_NAME).set(TableTask.FIELD_TASK_SN, taskSn));
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        return Optional.ofNullable(new TaskInfo()
            .setId(entity.getLong(TableTask.FIELD_ID))
            .setParams(entity.getStr(TableTask.FIELD_PARAMS))
            .setReadyAt(DateUtil.toLocalDateTime(entity.getDate(TableTask.FIELD_READY_AT)))
            .setTaskSn(entity.getStr(TableTask.FIELD_TASK_SN)));
    }
}
