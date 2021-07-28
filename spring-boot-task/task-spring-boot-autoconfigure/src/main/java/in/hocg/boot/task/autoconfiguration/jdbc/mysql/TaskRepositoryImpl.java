package in.hocg.boot.task.autoconfiguration.jdbc.mysql;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
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
import java.time.format.DateTimeFormatter;
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
    public List<TaskInfo> listByTypeAndReady(@NonNull Serializable taskType) {
        return Db.use(dataSource).find(Entity.create(
            TableTask.TABLE_NAME).set(TableTask.FIELD_TYPE, taskType).set(TableTask.FIELD_STATUS, TableTask.Status.Ready.getCode())
        ).stream().map(this::asTaskInfo).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public List<TaskInfo> listByReady() {
        return Db.use(dataSource).find(Entity.create(
            TableTask.TABLE_NAME).set(TableTask.FIELD_STATUS, TableTask.Status.Ready.getCode())
        ).stream().map(this::asTaskInfo).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows(SQLException.class)
    public TaskInfo createTask(@NonNull Serializable taskName, @NonNull Serializable taskType, @NonNull Serializable createUser, Object params, @NonNull Long delaySecond) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime readyAt = now.plusSeconds(delaySecond);

        String taskSn = IdUtil.objectId();
        String paramsStr = LangUtils.callIfNotNull(params, JSONUtil::toJsonStr).orElse(null);

        Entity entity = Entity.create(TableTask.TABLE_NAME)
            .setIgnoreNull(TableTask.FIELD_PARAMS, paramsStr)
            .set(TableTask.FIELD_TASK_SN, taskSn)
            .set(TableTask.FIELD_TITLE, taskName)
            .set(TableTask.FIELD_TYPE, taskType)
            .set(TableTask.FIELD_CREATOR, createUser)
            .set(TableTask.FIELD_CREATED_AT, now)
            .set(TableTask.FIELD_READY_AT, readyAt);
        Long id = Db.use(dataSource).insertForGeneratedKey(entity);
        entity.set(TableTask.FIELD_ID, id);

        return this.asTaskInfo(entity);
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
    public boolean startTask(@NonNull Serializable taskSn) {
        LocalDateTime now = LocalDateTime.now();
        Entity where = Entity.create(TableTask.TABLE_NAME)
            .set(TableTask.FIELD_TASK_SN, taskSn)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Ready.getCode());
        Entity update = Entity.create(TableTask.TABLE_NAME)
            .set(TableTask.FIELD_START_AT, now)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Executing.getCode());
        return Db.use(dataSource).update(update, where) > 0;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public boolean doneTask(@NonNull Serializable taskSn, @NonNull TableTask.DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data) {
        LocalDateTime now = LocalDateTime.now();
        Entity where = Entity.create(TableTask.TABLE_NAME)
            .set(TableTask.FIELD_TASK_SN, taskSn)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Executing.getCode());
        Entity update = Entity.create(TableTask.TABLE_NAME)
            .setIgnoreNull(TableTask.FIELD_DONE_MESSAGE, message)
            .setIgnoreNull(TableTask.FIELD_DONE_RESULT, LangUtils.callIfNotNull(data, JSONUtil::toJsonStr).orElse(null))
            .set(TableTask.FIELD_DONE_STATUS, doneStatus.getCode())
            .set(TableTask.FIELD_DONE_AT, now)
            .set(TableTask.FIELD_TOTAL_TIME_MILLIS, totalTimeMillis)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Done.getCode());
        return Db.use(dataSource).update(update, where) > 0;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Optional<TaskInfo> getByTaskSn(@NonNull Serializable taskSn) {
        Entity entity = Db.use(dataSource).get(Entity.create(TableTask.TABLE_NAME).set(TableTask.FIELD_TASK_SN, taskSn));
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.asTaskInfo(entity));
    }

    @Override
    public Integer deleteDays(@NonNull Long minusDays) {
        return this.deleteDays(minusDays, null, Lists.newArrayList(TableTask.DoneStatus.Success.getCode()));
    }

    @Override
    @SneakyThrows(SQLException.class)
    public Integer deleteDays(@NonNull Long minusDays, List<Serializable> eqTypes, List<Serializable> eqDoneStatus) {
        LocalDateTime preDateTime = LocalDateTime.now().minusDays(minusDays);
        Entity where = Entity.create(TableTask.TABLE_NAME)
            .set(TableTask.FIELD_STATUS, TableTask.Status.Done.getCode())
            .set(TableTask.FIELD_CREATED_AT, StrUtil.format("< {}", DateUtil.formatLocalDateTime(preDateTime)));
        if (Objects.nonNull(eqTypes)) {
            where = where.set(TableTask.FIELD_TYPE, StrUtil.format("in {}", ArrayUtil.join(eqTypes.toArray(), ",")));
        }
        if (Objects.nonNull(eqDoneStatus)) {
            where = where.set(TableTask.FIELD_DONE_RESULT, StrUtil.format("in {}", ArrayUtil.join(eqDoneStatus.toArray(), ",")));
        }
        return Db.use(dataSource).del(where);
    }

    private TaskInfo asTaskInfo(Entity entity) {
        String readyAtStr = entity.getStr(TableTask.FIELD_READY_AT);
        return new TaskInfo().setId(entity.getLong(TableTask.FIELD_ID))
            .setType(entity.getStr(TableTask.FIELD_TYPE))
            .setTaskSn(entity.getStr(TableTask.FIELD_TASK_SN))
            .setReadyAt(LangUtils.callIfNotNull(readyAtStr, s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))).orElse(null))
            .setParams(entity.getStr(TableTask.FIELD_PARAMS));
    }

}
