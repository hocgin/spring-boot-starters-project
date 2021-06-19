package in.hocg.boot.task.autoconfiguration.core;

import com.google.common.base.Stopwatch;
import in.hocg.boot.task.autoconfiguration.jdbc.TableTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.typetools.TypeResolver;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    private final TaskRepository repository;

    @Override
    public <T, R> TaskResult<R> runAsync(String taskSn, Function<T, R> runnable) {
        return this.runSync(taskSn, runnable);
    }

    @Override
    public <T, R> TaskResult<R> runSync(String taskSn, Function<T, R> runnable) {
        Stopwatch stopWatch = Stopwatch.createStarted();

        Optional<TaskInfo> taskOpt = getTask(taskSn);
        if (!taskOpt.isPresent()) {
            log.info("执行任务发生错误: 未找到任务编号:[{}]", taskSn);
            return TaskResult.fail("未找到任务");
        }
        TaskInfo taskInfo = taskOpt.get();
        LocalDateTime taskInfoReadyAt = taskInfo.getReadyAt();
        if (Objects.nonNull(taskInfoReadyAt) && LocalDateTime.now().isAfter(taskInfoReadyAt)) {
            log.info("任务未到执行时间, 任务编号:[{}]", taskSn);
            return TaskResult.fail("任务未到执行时间");
        }

        Serializable taskId = taskInfo.getId();
        TaskLogger.setTaskId(taskId);
        if (!repository.startTask(taskSn)) {
            log.info("任务已经执行或执行完成, 任务编号:[{}]", taskSn);
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
            isOk = false;
            errorMsg = e.getMessage();
            log.info("执行任务发生错误: 任务执行异常, 任务编号:[{}], 异常信息:[{}]", taskSn, e);
        } finally {
            long totalTimeMillis = stopWatch.stop().elapsed(TimeUnit.MILLISECONDS);
            TableTask.DoneStatus doneStatus = isOk ? TableTask.DoneStatus.Success : TableTask.DoneStatus.Fail;
            repository.doneTask(taskSn, doneStatus, totalTimeMillis, errorMsg, result);
            TaskLogger.clear();
        }
        return TaskResult.fail();
    }

    private Optional<TaskInfo> getTask(String taskSn) {
        return this.repository.getByTaskSn(taskSn);
    }

    private <R, T> R run(Function<T, R> runnable, T params) {
        return runnable.apply(params);
    }

}

