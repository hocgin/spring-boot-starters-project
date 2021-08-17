package in.hocg.boot.logging.autoconfiguration.core;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.base.Stopwatch;
import in.hocg.boot.logging.autoconfiguration.utils.LoggingUtils;
import in.hocg.boot.utils.StringPoolUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
import java.util.concurrent.TimeUnit;
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
    @Lazy
    @Autowired
    private LoggerAspect self;

    @Around("@annotation(annotation)")
    public Object pointcut(ProceedingJoinPoint point, UseLogger annotation) throws Throwable {
        Stopwatch watch = Stopwatch.createStarted();
        Object ret = null;
        String errorMessage = null;
        try {
            ret = point.proceed();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            throw e;
        } finally {
            self.handleLog(this.getRequest(), point, annotation, watch.stop(), ret, errorMessage);
        }
        return ret;
    }

    @Async
    @SneakyThrows
    protected void handleLog(Optional<HttpServletRequest> requestOpt, ProceedingJoinPoint point, UseLogger annotation, Stopwatch watch, Object ret, String errorMessage) {
        // 日志收集
        Object target = point.getTarget();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Class<?> aClass = point.getSourceLocation().getWithinType();
        String mapping = String.format("%s#%s(%s)", aClass.getName(), methodName, Arrays.toString(parameterNames));
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(methodName, parameterTypes);
        List<Object> args = CollectionUtil.newArrayList(point.getArgs())
            .parallelStream()
            .filter(arg -> (!(arg instanceof ServletRequest)
                && !(arg instanceof ServletResponse)
                && !(arg instanceof InputStreamSource)
                && !(arg instanceof InputStream))
                && !(arg instanceof OutputStream))
            .collect(Collectors.toList());

        String uri = StringPoolUtils.UNKNOWN;
        String requestMethod = StringPoolUtils.UNKNOWN;
        String host = StringPoolUtils.UNKNOWN;
        String userAgent = StringPoolUtils.UNKNOWN;
        String source = StringPoolUtils.UNKNOWN;
        String username = null;
        String clientIp = "0.0.0.0";
        if (requestOpt.isPresent()) {
            HttpServletRequest request = requestOpt.get();
            uri = request.getRequestURI();
            requestMethod = request.getMethod();
            userAgent = LoggingUtils.getUserAgent(request);
            host = LoggingUtils.getHost(request);
            clientIp = LoggingUtils.getClientIp(request);
            source = request.getHeader(StringPoolUtils.HEADER_SOURCE);
            username = request.getHeader(StringPoolUtils.HEADER_USERNAME);
        }
        String enterRemark = annotation.value();
        if (Strings.isBlank(enterRemark) && LoggingUtils.hasApiOperation()) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            enterRemark = apiOperation.value();
        }

        LoggerEvent logger = new LoggerEvent().setMapping(mapping)
            .setCurrentUser(this.getCurrentUser().orElse(username))
            .setSource(source).setException(errorMessage)
            .setEnterRemark(enterRemark).setHost(host)
            .setCreatedAt(LocalDateTime.now()).setClientIp(clientIp)
            .setUserAgent(userAgent).setMethod(requestMethod)
            .setTotalTimeMillis(watch.elapsed(TimeUnit.MILLISECONDS))
            .setUri(uri).setRet(ret).setArgs(args);
        publisher.publishEvent(logger);
    }

    private Optional<HttpServletRequest> getRequest() {
        try {
            return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Object> getCurrentUser() {
        if (LoggingUtils.hasSecurityContextHolders()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(auth) || auth instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }
            return Optional.of(auth.getPrincipal());
        }
        return Optional.empty();
    }
}
