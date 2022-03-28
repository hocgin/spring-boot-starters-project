package in.hocg.boot.mybatis.plus.extensions.sample.task;

import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.extensions.task.TaskMpeService;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import in.hocg.boot.mybatis.plus.extensions.task.support.FailStrategy;
import in.hocg.boot.utils.Rules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ScheduledTask {
    private final TaskMpeService taskMpeService;
    public final static String TASK_TYPE = "auto_task";

    @Scheduled(cron = "0/30 0/1 * * * ?  ")
    public void taskCenter() {
        taskMpeService.listByReady().forEach(taskItem -> Rules.create()
            .rule(TASK_TYPE, () -> taskMpeService.runAsync(taskItem.getId(), this::throwException, FailStrategy.debug()))
            .rule(TASK_TYPE, () -> taskMpeService.runAsync(taskItem.getId(), this::throwException, FailStrategy.skipAndLog()))
            .rule(TASK_TYPE, () -> taskMpeService.runAsync(taskItem.getId(), this::throwException, FailStrategy.skip()))
            .rule(TASK_TYPE, () -> taskMpeService.runAsync(taskItem.getId(), this::throwException, FailStrategy.reCreate()))
            .rule(TASK_TYPE, () -> taskMpeService.runAsync(taskItem.getId(), this::throwException, FailStrategy.reCreateLevel(2L)))
            .of(taskItem.getType()));
        taskMpeService.doneExpiredDays(10L);
    }

    private Object myTask(String nil) {
        log.info("任务执行过程: {}", nil);
        taskMpeService.deleteDays(10L);
        taskMpeService.deleteDays(10L, null, Lists.newArrayList(TaskItem.DoneStatus.Fail, TaskItem.DoneStatus.Success));
        return null;
    }

    private Object throwException(String nil) {
        throw new RuntimeException("我是异常");
    }

    @Scheduled(cron = "0/30 * * * * ? ")
    public void pushTask() {
        taskMpeService.createTask("自动任务", ScheduledTask.TASK_TYPE, new Object());
        log.info("==> 提交任务");
    }
}
