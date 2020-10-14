package in.hocg.boot.logging.autoconfiguration.core;

import cn.hutool.core.collection.CollectionUtil;
import in.hocg.boot.logging.autoconfiguration.utils.ClassName;
import in.hocg.boot.logging.autoconfiguration.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hocgin
 * @date 2019/6/25
 */
@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class LoggerAspect {
    private final ApplicationEventPublisher publisher;
    @Autowired
    @Lazy
    private LoggerAspect self;

    @Pointcut("@annotation(in.hocg.boot.logging.autoconfiguration.core.UseLogger)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 耗时统计
        StopWatch watch = new StopWatch();
        watch.start();
        Object ret = null;
        String errorMessage = null;
        try {
            ret = point.proceed();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            throw e;
        } finally {
            watch.stop();
            // 日志收集
            Object target = point.getTarget();
            MethodSignature signature = (MethodSignature) point.getSignature();
            String methodName = signature.getName();
            String[] parameterNames = signature.getParameterNames();
            Class<?> aClass = point.getSourceLocation().getWithinType();
            String mapping = String.format("%s#%s(%s)", aClass.getName(), methodName, Arrays.toString(parameterNames));
            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            UseLogger annotation = method.getAnnotation(UseLogger.class);
            List<Object> args = CollectionUtil.newArrayList(point.getArgs())
                .parallelStream()
                .filter(arg -> (!(arg instanceof ServletRequest)
                    && !(arg instanceof ServletResponse)
                    && !(arg instanceof InputStreamSource)
                    && !(arg instanceof InputStream))
                    && !(arg instanceof OutputStream))
                .collect(Collectors.toList());

            Optional<HttpServletRequest> requestOpt = this.getRequest();
            String uri = "Unknown";
            String requestMethod = "Unknown";
            String host = "Unknown";
            String userAgent = "Unknown";
            String clientIp = "0.0.0.0";
            String source = null;
            if (requestOpt.isPresent()) {
                final HttpServletRequest request = requestOpt.get();
                uri = request.getRequestURI();
                requestMethod = request.getMethod();
                userAgent = RequestUtils.getUserAgent(request);
                host = RequestUtils.getHost(request);
                clientIp = RequestUtils.getClientIp(request);
                source = request.getParameter("source");
            }
            final LoggerEvent logger = new LoggerEvent();
            final String enterRemark = annotation.value();
            logger.setMapping(mapping)
                .setCurrentUser(this.getCurrentUser().orElse(null))
                .setSource(source)
                .setException(errorMessage)
                .setEnterRemark(enterRemark)
                .setHost(host)
                .setCreatedAt(LocalDateTime.now())
                .setClientIp(clientIp)
                .setUserAgent(userAgent)
                .setMethod(requestMethod)
                .setTotalTimeMillis(watch.getTotalTimeMillis())
                .setUri(uri)
                .setRet(ret)
                .setArgs(args);
            self.sendAsync(logger);
        }

        return ret;
    }

    private Optional<Object> getCurrentUser() {
        if (ClassName.hasSecurityContextHolders()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(auth) || auth instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }
            return Optional.of(auth.getPrincipal());
        }
        return Optional.empty();
    }

    @Async
    protected void sendAsync(LoggerEvent logger) {
        publisher.publishEvent(logger);
    }

    private Optional<HttpServletRequest> getRequest() {
        try {
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return Optional.of(request);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
