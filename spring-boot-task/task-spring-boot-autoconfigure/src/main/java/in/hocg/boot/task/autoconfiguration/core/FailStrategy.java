package in.hocg.boot.task.autoconfiguration.core;


import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.task.autoconfiguration.core.dto.ExecTaskDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Created by hocgin on 2021/10/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class FailStrategy {

    /**
     * 重新创建策略
     *
     * @param delaySecond 下次执行时间
     * @param maxCount    最大创建数量
     * @return 策略
     */
    public static Consumer<ExecTaskDTO> reCreate(Long delaySecond, Long maxCount) {
        return task -> {
            SpringUtil.getBean(TaskRepository.class).reCreateExecTask(task.getTaskId(), delaySecond, maxCount);
        };
    }

    /**
     * 失败重新创建
     *
     * @return 默认创建策略
     */
    public static Consumer<ExecTaskDTO> reCreate() {
        return FailStrategy.reCreate(60L, 12L);
    }

    public static Consumer<ExecTaskDTO> debug() {
        return FailStrategy.reCreate(5L, 100L);
    }

    /**
     * 失败跳过
     *
     * @return 策略
     */
    public static Consumer<ExecTaskDTO> skip() {
        return task -> {
        };
    }

    /**
     * 跳过并进行记录
     *
     * @return 策略
     */
    public static Consumer<ExecTaskDTO> skipAndLog() {
        return task -> {
            log.error("任务执行失败, 任务ID: {}", task.getTaskId());
        };
    }
}
