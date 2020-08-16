package in.hocg.boot.web;

import in.hocg.boot.web.utils.web.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author hocgin
 * @date 2019/7/19
 */
@Slf4j
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }


    /**
     * 通过名字获取上下文中的bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return ((T) APPLICATION_CONTEXT.getBean(name));
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBean(requiredType);
    }

    public static Optional<HttpServletResponse> getResponse() {
        try {
            final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            return Optional.ofNullable(response);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<HttpServletRequest> getRequest() {
        try {
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return Optional.of(request);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<HttpSession> getSession() {
        return getRequest().map(request -> request.getSession(false));
    }

    /**
     * 请求的客户端
     *
     * @return
     */
    public static Optional<String> getClientIp() {
        return SpringContext.getRequest().map(RequestUtils::getClientIP);
    }
}
