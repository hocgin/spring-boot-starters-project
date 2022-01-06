package in.hocg.boot.task.autoconfiguration.core;

import com.google.common.base.Stopwatch;
import in.hocg.boot.task.autoconfiguration.core.dto.ExecTaskDTO;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.typetools.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class TaskBerviceImpl implements TaskBervice {
    @Lazy
    @Autowired(required = false)
    private TaskRepository repository;

    @Override
    public <T, R> Future<TaskResult<R>> runAsync(Long taskItemId, Function<T, R> runnable, Consumer<ExecTaskDTO> failStrategy) {
        return AsyncResult.forValue(this.runSync(taskItemId, runnable, failStrategy));
    }

    @Override
    public <T, R> TaskResult<R> runSync(Long taskItemId, Function<T, R> runnable, Consumer<ExecTaskDTO> failStrategy) {
        Stopwatch stopWatch = Stopwatch.createStarted();

        Optional<ExecTaskDTO> taskOpt = getExecTask(taskItemId);
        if (!taskOpt.isPresent()) {
            log.info("执行任务发生错误: 未找到任务项编号:[{}]", taskItemId);
            return TaskResult.fail("未找到任务");
        }
        ExecTaskDTO taskInfo = taskOpt.get();
        Long taskId = taskInfo.getTaskId();

        LocalDateTime taskInfoReadyAt = taskInfo.getReadyAt();
        if (Objects.nonNull(taskInfoReadyAt) && LocalDateTime.now().isBefore(taskInfoReadyAt)) {
            log.info("任务未到执行时间, 任务项编号:[{}-{}]", taskId, taskItemId);
            return TaskResult.fail("任务未到执行时间");
        }

        TaskLogger.setTaskId(taskItemId);
        if (!repository.startTask(taskItemId)) {
            log.info("任务已经执行或执行完成, 任务项编号:[{}-{}]", taskId, taskItemId);
            return TaskResult.fail("任务已经执行");
        }

        boolean isOk = true;
        String errorMsg = "ok";
        R result = null;
        try {
            Class<?>[] typeArgs = TypeResolver.resolveRawArguments(Function.class, runnable.getClass());
            result = this.run(runnable, taskInfo.resolveParams(typeArgs[0]));
            return TaskResult.success(result);
        } catch (Exception e) {
            log.info("执行任务发生错误: 任务执行异常, 任务项编号:[{}-{}], 异常信息:[{}]", taskId, taskItemId, e);
            isOk = false;
            errorMsg = e.getMessage();
            if (Objects.nonNull(failStrategy)) {
                failStrategy.accept(taskInfo);
            }
        } finally {
            long totalTimeMillis = stopWatch.stop().elapsed(TimeUnit.MILLISECONDS);
            TaskItem.DoneStatus doneStatus = isOk ? TaskItem.DoneStatus.Success : TaskItem.DoneStatus.Fail;
            repository.doneTask(taskItemId, doneStatus, totalTimeMillis, errorMsg, result);
            TaskLogger.clear();
        }
        return TaskResult.fail();
    }

    private Optional<ExecTaskDTO> getExecTask(Long taskItemId) {
        return this.repository.getByTaskItemId(taskItemId);
    }


    private <R, T> R run(Function<T, R> runnable, T params) {
        return runnable.apply(params);
    }

}

