package in.hocg.boot.task.autoconfiguration.core;


import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
    public static Consumer<TaskItem> reCreate(Long delaySecond, Long maxCount) {
        return task -> {
            SpringUtil.getBean(TaskRepository.class).reCreateExecTask(task.getTaskId(), delaySecond, maxCount);
        };
    }

    /**
     * 根据级别进行创建策略
     *
     * @param delaySecondLevel
     * @param maxCount
     * @return
     */
    public static Consumer<TaskItem> reCreateLevel(List<Long> delaySecondLevel, Long maxCount) {
        return task -> {
            int idx = task.getIdx() + 1;
            Long delaySecond = delaySecondLevel.get(idx % delaySecondLevel.size());
            reCreate(delaySecond, maxCount);
        };
    }

    /**
     * 默认级别重建
     *
     * @param maxCount
     * @return
     */
    public static Consumer<TaskItem> reCreateLevel(Long maxCount) {
        // 1m 2m 5m 5m 5m 30m 1h 2h 4h 8h 12h 24h
        return reCreateLevel(Lists.newArrayList(60L, 2 * 60L, 5 * 60L, 5 * 60L, 5 * 60L, 30 * 60L,
            60 * 60L, 2 * 60 * 60L, 4 * 60 * 60L, 8 * 60 * 60L, 12 * 60 * 60L, 24 * 60 * 60L), maxCount);
    }

    /**
     * 失败重新创建
     *
     * @return 默认创建策略
     */
    public static Consumer<TaskItem> reCreate() {
        return FailStrategy.reCreate(5 * 60L, 12L);
    }

    /**
     * 失败跳过
     *
     * @return 策略
     */
    public static Consumer<TaskItem> skip() {
        return task -> {
        };
    }

    /**
     * 跳过并进行记录
     *
     * @return 策略
     */
    public static Consumer<TaskItem> skipAndLog() {
        return task -> {
            log.error("任务执行失败, 任务ID: {}", task.getTaskId());
        };
    }


    public static Consumer<TaskItem> debug() {
        return FailStrategy.reCreate(5L, 100L);
    }
}
