package in.hocg.boot.cache.autoconfiguration.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 延迟队列
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class RedisDelayedQueue {

    private final RedissonClient redissonClient;

    /**
     * 添加到队列
     *
     * @param data     数据对象
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <T>      泛型
     */
    public <T> void addQueue(T data, long delay, TimeUnit timeUnit) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(data.getClass().getName());
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(data, delay, timeUnit);
        delayedQueue.destroy();
    }

    /**
     * 获取队列
     *
     * @param zClass            数据对象类型，用于获取队列
     * @param taskEventListener 任务回调监听
     * @param <T>               泛型
     */
    public <T> void getQueue(Class<?> zClass, Consumer<T> taskEventListener) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(zClass.getName());
        // 由于此线程需要常驻，可以新建线程，不用交给线程池管理
        ((Runnable) () -> {
            while (true) {
                try {
                    taskEventListener.accept(blockingFairQueue.take());
                } catch (InterruptedException e) {
                    log.error("程序中断", e);
                }
            }
        }).run();
    }


}
