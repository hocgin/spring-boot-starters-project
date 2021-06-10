package in.hocg.boot.task.autoconfiguration;

import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.TaskService;
import in.hocg.boot.task.autoconfiguration.core.TaskServiceImpl;
import in.hocg.boot.task.autoconfiguration.jdbc.mysql.TaskRepositoryImpl;
import in.hocg.boot.task.autoconfiguration.properties.TaskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableAsync
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnBean({DataSource.class})
@ConditionalOnProperty(prefix = TaskProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(TaskProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaskAutoConfiguration {
    public static final String EXECUTOR_NAME = "bootAsyncTaskExecutor";

    @Bean(TaskAutoConfiguration.EXECUTOR_NAME)
    public Executor bootAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("boot-async-task-service-");
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskService taskService(TaskRepository repository) {
        return new TaskServiceImpl(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskRepository taskRepository(DataSource dataSource) {
        return new TaskRepositoryImpl(dataSource);
    }
}
