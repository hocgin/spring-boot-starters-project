package in.hocg.boot.mybatis.plus.extensions.task.autoconfiguration;

import in.hocg.boot.mybatis.plus.extensions.task.TaskMpe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@EnableAsync
@Configuration
@ConditionalOnProperty(prefix = TaskProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(TaskProperties.class)
@ComponentScan(TaskMpe.PACKAGE)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaskMybatisPlusExtAutoConfiguration {
    private final TaskProperties properties;
    public static final String EXECUTOR_NAME = "bootAsyncTaskExecutor";

    @Bean(TaskMybatisPlusExtAutoConfiguration.EXECUTOR_NAME)
    public Executor bootAsyncTaskExecutor() {
        TaskProperties.Executor executor = properties.getExecutor();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(executor.getCorePoolSize());
        taskExecutor.setMaxPoolSize(executor.getMaxPoolSize());
        taskExecutor.setQueueCapacity(executor.getQueueCapacity());
        taskExecutor.setThreadNamePrefix(executor.getThreadNamePrefix());
        return taskExecutor;
    }
}
