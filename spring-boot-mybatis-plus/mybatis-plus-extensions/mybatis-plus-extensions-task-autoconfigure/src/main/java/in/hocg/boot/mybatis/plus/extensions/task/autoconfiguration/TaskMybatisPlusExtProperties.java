package in.hocg.boot.mybatis.plus.extensions.task.autoconfiguration;

import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ConfigurationProperties(TaskMybatisPlusExtProperties.PREFIX)
public class TaskMybatisPlusExtProperties {
    public static final String PREFIX = MyBatisPlusExtensionsConstants.PROPERTIES_PREFIX + ".task";

    /**
     * 执行器配置
     */
    private Executor executor = Executor.DEFAULT;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Executor {
        public static final Executor DEFAULT = new Executor()
            .setThreadNamePrefix("boot-async-task-")
            // size: 5k * 110, core: 5, max: 10, num: 105
            .setQueueCapacity(100)
            .setCorePoolSize(5)
            .setMaxPoolSize(10);

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
