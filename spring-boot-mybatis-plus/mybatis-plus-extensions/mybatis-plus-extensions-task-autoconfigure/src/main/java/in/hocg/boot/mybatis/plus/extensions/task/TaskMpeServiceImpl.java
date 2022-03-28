package in.hocg.boot.mybatis.plus.extensions.task;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Stopwatch;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItemLog;
import in.hocg.boot.mybatis.plus.extensions.task.pojo.dto.TaskInfoItemDTO;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskInfoMpeService;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskItemLogMpeService;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskItemMpeService;
import in.hocg.boot.mybatis.plus.extensions.task.support.TaskHelper;
import in.hocg.boot.mybatis.plus.extensions.task.support.TaskResult;
import in.hocg.boot.utils.LangUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.typetools.TypeResolver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaskMpeServiceImpl implements TaskMpeService {
    private final TaskInfoMpeService taskInfoMpeService;
    private final TaskItemMpeService taskItemMpeService;
    private final TaskItemLogMpeService taskItemLogMpeService;

    @Override
    public TaskItem createTask(@NonNull String taskName, @NonNull String taskType, Object params, Long delaySecond) {
        String taskSn = IdUtil.objectId();
        String paramsStr = LangUtils.callIfNotNull(params, JSONUtil::toJsonStr).orElse(null);

        TaskInfo taskInfo = new TaskInfo()
            .setTitle(taskName)
            .setParams(paramsStr)
            .setType(taskType)
            .setRetryCount(0)
            .setTaskSn(taskSn);
        taskInfoMpeService.saveOrUpdate(taskInfo);
        return createExecTaskByTask(taskInfo, LangUtils.getOrDefault(delaySecond, 10L), 5L);
    }

    @Override
    public Optional<TaskInfoItemDTO> getLastTaskId(Long taskId) {
        return getByTaskId(taskId).flatMap(taskInfo -> {
            Integer retryCount = ObjectUtil.defaultIfNull(taskInfo.getRetryCount(), 0);
            if (retryCount == 0) {
                return Optional.empty();
            }
            return getByTaskIdAndIdx(taskId, taskInfo.getRetryCount())
                .map(taskItem -> TaskInfoItemDTO.as(taskInfo, taskItem));
        });
    }

    @Override
    public Optional<TaskItem> getByTaskIdAndIdx(Long taskId, Integer idx) {
        TaskItem taskItem = new TaskItem()
            .setTaskId(taskId).setIdx(idx)
            .setStatus(TaskItem.Status.Done.getCode());
        taskItemMpeService.saveOrUpdate(taskItem);
        return Optional.ofNullable(taskItem);
    }

    @Override
    public void log(@NonNull Long taskItemId, String message) {
        TaskItemLog taskLog = new TaskItemLog()
            .setContent(message)
            .setTaskItemId(taskItemId);
        taskItemLogMpeService.saveOrUpdate(taskLog);
    }

    @Override
    public boolean start(@NonNull Long taskItemId) {
        return taskItemMpeService.start(taskItemId);
    }

    @Override
    public void done(@NonNull Long taskItemId, TaskItem.@NonNull DoneStatus doneStatus, @NonNull Long totalTimeMillis, String message, Object data) {
        taskItemMpeService.done(taskItemId, doneStatus, totalTimeMillis, message, data);
    }

    @Override
    public Optional<TaskItem> getByTaskItemId(Long taskItemId) {
        return Optional.ofNullable(taskItemMpeService.getById(taskItemId));
    }

    @Override
    public Optional<TaskInfo> getByTaskId(Long taskId) {
        return Optional.ofNullable(taskInfoMpeService.getById(taskId));
    }

    @Override
    public void reCreateExecTask(Long taskId, Long delaySecond, Long maxCount) {
        TaskInfo taskInfo = taskInfoMpeService.getById(taskId);
        if (Objects.isNull(taskInfo)) {
            return;
        }
        createExecTaskByTask(taskInfo, delaySecond, maxCount);
    }

    @Override
    public TaskItem createExecTaskByTask(TaskInfo taskInfo, Long delaySecond, Long maxCount) {
        return taskItemMpeService.createExecTaskByTask(taskInfo, delaySecond, maxCount);
    }

    @Override
    public Boolean deleteDays(@NonNull Long minusDays, List<String> eqTypes, List<TaskItem.DoneStatus> eqStatus) {
        return taskItemMpeService.deleteDays(minusDays, eqTypes, eqStatus);
    }

    @Override
    public Boolean doneExpiredDays(@NonNull Long minusDays, List<String> types) {
        return taskItemMpeService.doneExpiredDays(minusDays, types);
    }

    @Override
    public List<TaskItem> listByTypeAndReady(@NonNull String taskType) {
        return taskItemMpeService.listByTypeAndStatus(taskType, TaskItem.Status.Ready.getCodeStr());
    }

    @Override
    public List<TaskItem> listByReady() {
        return taskItemMpeService.listByTypeAndStatus(null, TaskItem.Status.Ready.getCodeStr());
    }

    @Override
    public <T, R> TaskResult<R> runSync(Long taskItemId, Function<T, R> runnable, Consumer<TaskItem> failStrategy) {
        Stopwatch stopWatch = Stopwatch.createStarted();

        Optional<TaskItem> taskOpt = getByTaskItemId(taskItemId);
        if (taskOpt.isEmpty()) {
            log.info("执行任务发生错误: 未找到任务项编号:[{}]", taskItemId);
            return TaskResult.fail("未找到任务");
        }
        TaskItem taskInfo = taskOpt.get();
        Long taskId = taskInfo.getTaskId();

        LocalDateTime taskInfoReadyAt = taskInfo.getReadyAt();
        if (Objects.nonNull(taskInfoReadyAt) && LocalDateTime.now().isBefore(taskInfoReadyAt)) {
            log.info("任务未到执行时间, 任务项编号:[{}-{}]", taskId, taskItemId);
            return TaskResult.fail("任务未到执行时间");
        }

        boolean isOk = true;
        String errorMsg = "ok";
        R result = null;
        try {
            if (!start(taskItemId)) {
                log.info("任务已经执行或执行完成, 任务项编号:[{}-{}]", taskId, taskItemId);
                return TaskResult.fail("任务已经执行");
            }
            Class<?>[] typeArgs = TypeResolver.resolveRawArguments(Function.class, runnable.getClass());
            result = this.run(runnable, TaskHelper.resolveParams(taskInfo, typeArgs[0]));
            return TaskResult.success(result);
        } catch (Exception e) {
            log.info("执行任务发生错误: 任务执行异常, 任务项编号:[{}-{}], 异常信息:", taskId, taskItemId, e);
            isOk = false;
        } finally {
            long totalTimeMillis = stopWatch.stop().elapsed(TimeUnit.MILLISECONDS);
            TaskItem.DoneStatus doneStatus = isOk ? TaskItem.DoneStatus.Success : TaskItem.DoneStatus.Fail;
            this.done(taskItemId, doneStatus, totalTimeMillis, errorMsg, result);
            if (!isOk && Objects.nonNull(failStrategy)) {
                failStrategy.accept(taskInfo);
            }
        }
        return TaskResult.fail();
    }

    private <R, T> R run(Function<T, R> runnable, T params) {
        return runnable.apply(params);
    }
}
