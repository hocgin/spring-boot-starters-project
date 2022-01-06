package in.hocg.boot.task.sample;

import com.google.common.collect.Lists;
import in.hocg.boot.task.autoconfiguration.core.FailStrategy;
import in.hocg.boot.task.autoconfiguration.core.TaskBervice;
import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
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
    private final TaskRepository taskRepository;
    private final TaskBervice taskService;
    public final static String TASK_TYPE = "auto_task";

    @Scheduled(cron = "0/30 0/1 * * * ?  ")
    public void taskCenter() {
        taskRepository.listByTypeAndReady(ScheduledTask.TASK_TYPE)
            .forEach(taskInfo -> taskService.runAsync(taskInfo.getId(), this::throwException, FailStrategy.debug()));
    }

    private Object myTask(String nil) {
        log.info("任务执行过程: {}", nil);
        taskRepository.deleteDays(10L);
        taskRepository.deleteDays(10L, null, Lists.newArrayList(TaskItem.DoneStatus.Fail, TaskItem.DoneStatus.Success));
        return null;
    }

    private Object throwException(String nil) {
        throw new RuntimeException("我是异常");
    }

    @Scheduled(cron = "0/30 * * * * ? ")
    public void pushTask() {
//        taskRepository.createTask("自动任务", ScheduledTask.TASK_TYPE, new Object());
        log.info("==> 提交任务");
    }
}
