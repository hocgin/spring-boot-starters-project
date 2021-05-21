package in.hocg.boot.message.autoconfigure.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by hocgin on 2019-09-29.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TransactionalAspect {
    private final ApplicationEventPublisher publisher;

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void aspect() {
    }

    @Around("aspect()")
    public Object doAspect(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        publisher.publishEvent(new TransactionalEvent());
        return proceed;
    }
}
