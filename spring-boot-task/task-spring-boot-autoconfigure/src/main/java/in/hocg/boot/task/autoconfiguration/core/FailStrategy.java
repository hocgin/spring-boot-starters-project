package in.hocg.boot.task.autoconfiguration.core;


import cn.hutool.extra.spring.SpringUtil;

import java.util.function.Function;

/**
 * Created by hocgin on 2021/10/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */

/**
 * 失败策略
 */
public class FailStrategy {

    /**
     * 重建任务
     *
     * @param taskSn
     * @param runnable
     * @param delaySecond
     * @param maxCount
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Function<T, R> reCreate(String taskSn, Function<T, R> runnable, long delaySecond, long maxCount) {
        return t -> {
            try {
                return runnable.apply(t);
            } catch (Exception e) {
                SpringUtil.getBean(TaskRepository.class).reCreateTask(taskSn, delaySecond, maxCount);
                throw e;
            }
        };
    }

    public static <T, R> Function<T, R> reCreate(String taskSn, Function<T, R> runnable) {
        return FailStrategy.reCreate(taskSn, runnable, 3, 12);
    }
}
