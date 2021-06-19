package in.hocg.boot.task.autoconfiguration;

import in.hocg.boot.task.autoconfiguration.core.TaskRepository;
import in.hocg.boot.task.autoconfiguration.core.TaskBervice;
import in.hocg.boot.task.autoconfiguration.core.TaskBerviceImpl;
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
    private final TaskProperties properties;
    public static final String EXECUTOR_NAME = "bootAsyncTaskExecutor";

    @Bean(TaskAutoConfiguration.EXECUTOR_NAME)
    public Executor bootAsyncTaskExecutor() {
        TaskProperties.Executor executor = properties.getExecutor();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(executor.getCorePoolSize());
        taskExecutor.setMaxPoolSize(executor.getMaxPoolSize());
        taskExecutor.setQueueCapacity(executor.getQueueCapacity());
        taskExecutor.setThreadNamePrefix(executor.getThreadNamePrefix());
        return taskExecutor;
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskBervice taskBervice(TaskRepository repository) {
        return new TaskBerviceImpl(repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskRepository taskRepository(DataSource dataSource) {
        return new TaskRepositoryImpl(dataSource);
    }
}
