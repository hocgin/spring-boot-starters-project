package in.hocg.boot.task.sample;

import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.TaskBervice;
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
    private final static String TASK_TYPE = "auto_task";

    @Scheduled(cron = "0/30 0/1 * * * ?  ")
    public void taskCenter() {
        taskRepository.listByTypeAndReady(ScheduledTask.TASK_TYPE)
            .forEach(taskInfo -> taskService.runAsync(taskInfo.getTaskSn(), this::myTask));
    }

    private Object myTask(String nil) {
        log.info("任务执行过程: {}", nil);
        return null;
    }

    @Scheduled(cron = "0/30 * * * * ? ")
    public void pushTask() {
        taskRepository.createTask("自动任务", TASK_TYPE, 0, null);
        log.info("==> 提交任务");
    }
}
