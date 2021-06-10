package in.hocg.boot.task.autoconfiguration.core;

import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.utils.lambda.SFunction;
import in.hocg.boot.utils.lambda.SerializedLambda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StopWatch;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class TaskServiceImpl implements TaskService, InitializingBean {
    private TaskRepository repository;

    @Override
    public <T, R> TaskResult<R> runAsync(String taskSn, SFunction<T, R> runnable) {
        return this.runSync(taskSn, runnable);
    }

    @Override
    public <T, R> TaskResult<R> runSync(String taskSn, SFunction<T, R> runnable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(taskSn);

        Optional<TaskInfo> taskOpt = getTask(taskSn);
        if (!taskOpt.isPresent()) {
            log.info("执行任务发生错误: 未找到任务编号:[{}]", taskSn);
            return TaskResult.fail();
        }
        TaskInfo taskInfo = taskOpt.get();
        Serializable taskId = taskInfo.getId();
        TaskLogger.setTaskId(taskId);
        repository.startTask(taskSn);

        boolean isOk = true;
        String errorMsg = "ok";
        R result = null;
        try {
            Class<T> paramClazz = SerializedLambda.resolve(runnable).getInstantiatedMethodType();
            result = this.run(runnable, taskInfo.resolveParams(paramClazz));
            return TaskResult.success(result);
        } catch (Exception e) {
            isOk = false;
            errorMsg = e.getMessage();
            log.info("执行任务发生错误: 任务执行异常, 任务编号:[{}], 异常信息:[{}]", taskSn, e);
        } finally {
            stopWatch.stop();
            repository.doneTask(taskSn, isOk, stopWatch.getTotalTimeMillis(), errorMsg, result);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        this.repository = SpringUtil.getBean(TaskRepository.class);
    }
}

