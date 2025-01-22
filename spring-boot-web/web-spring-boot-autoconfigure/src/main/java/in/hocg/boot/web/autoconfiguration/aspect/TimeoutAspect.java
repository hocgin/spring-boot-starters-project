package in.hocg.boot.web.autoconfiguration.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

@Slf4j
@Aspect
public class TimeoutAspect {

    @Pointcut("@annotation(timeoutCheck)")
    public void pointCut(TimeoutCheck timeoutCheck) {
    }

    @Around(value = "pointCut(timeoutCheck)", argNames = "joinPoint, timeoutCheck")
    public Object around(ProceedingJoinPoint joinPoint, TimeoutCheck timeoutCheck) throws Throwable {
        long timeout = timeoutCheck.timeout();
        if (timeout <= 0) {
            return joinPoint.proceed();
        }

        String threadPool = timeoutCheck.threadPool();
        FutureTask<Object> futureTask = createTask(joinPoint);
        try {
            runWithThread(threadPool, futureTask);
            return futureTask.get(timeout, timeoutCheck.unit());
        } finally {
            futureTask.cancel(timeoutCheck.destroy());
        }
    }

    private static void runWithThread(String threadPool, FutureTask futureTask) {
        Executor bean = null;
        if (StrUtil.isNotBlank(threadPool)) {
            bean = SpringContext.getBean(threadPool);
        }
        if (Objects.nonNull(bean)) {
            bean.execute(futureTask);
        } else {
            Thread thread = new Thread(futureTask, "TimeoutAspect");
            thread.start();
        }
    }

    private static FutureTask<Object> createTask(ProceedingJoinPoint joinPoint) {
        return new FutureTask<>(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}
