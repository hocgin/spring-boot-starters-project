package in.hocg.boot.task.sample;

import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.dto.TaskDTO;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduledTaskTest extends AbstractSpringBootTest {
    @Autowired
    TaskRepository taskRepository;

    @org.junit.Test
    public void pushTask() {
        TaskItem task = taskRepository.createTask("自动任务", ScheduledTask.TASK_TYPE, new Object());
    }

    @org.junit.Test
    public void getLastTaskId() {
        Optional<TaskDTO> task = taskRepository.getLastTaskId(1L);
        System.out.println(task.orElse(null));
    }
}
