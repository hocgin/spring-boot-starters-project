package in.hocg.boot.mybatis.plus.extensions.dataaudit.autoconfiguration.aspect;

import in.hocg.boot.mybatis.plus.extensions.dataaudit.autoconfiguration.annotation.IgnoreTenant;
import in.hocg.boot.utils.context.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.ContentHandler;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class TenantIgnoreAspect {
    /**
     * 切入点
     */
    @Pointcut("@within(in.hocg.boot.mybatis.plus.extensions.dataaudit.autoconfiguration.annotation.IgnoreTenant) ||@annotation(in.hocg.boot.mybatis.plus.extensions.dataaudit.autoconfiguration.annotation.IgnoreTenant)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            Class<?> targetClass = point.getTarget().getClass();
            IgnoreTenant classIgnoreTenant = targetClass.getAnnotation(IgnoreTenant.class);
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            IgnoreTenant methodIgnoreTenant = method.getAnnotation(IgnoreTenant.class);

            //判断类上是否有注解
            boolean isClassAnnotated = AnnotationUtils.isAnnotationDeclaredLocally(IgnoreTenant.class, targetClass);
            //判断方法上是否有注解
            boolean isMethodAnnotated = Objects.nonNull(methodIgnoreTenant);

            //如果类上有
            if (isClassAnnotated) {
                UserContextHolder.setIgnoreTenant(classIgnoreTenant.ignore());
            }

            //如果方法上有 以方法上的为主
            if (isMethodAnnotated) {
                UserContextHolder.setIgnoreTenant(classIgnoreTenant.ignore());
            }
            return point.proceed();
        } finally {
            UserContextHolder.setIgnoreTenant(null);
        }
    }
}
