package in.hocg.boot.task.autoconfiguration.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(TaskProperties.PREFIX)
public class TaskProperties {
    public static final String PREFIX = "boot.task";

    /**
     * 执行器配置
     */
    private Executor executor = Executor.DEFAULT;

    @Data
    @Accessors(chain = true)
    public static class Executor {
        public static final Executor DEFAULT = new Executor()
            .setThreadNamePrefix("boot-async-task-")
            .setQueueCapacity(1000)
            .setCorePoolSize(2)
            .setMaxPoolSize(5);

        /**
         * 核心数量
         */
        private Integer corePoolSize;
        /**
         * 最大数量
         */
        private Integer maxPoolSize;
        /**
         * 等待容量
         */
        private Integer queueCapacity;
        /**
         * 执行器前缀
         */
        private String threadNamePrefix;
    }
}
