package in.hocg.boot.web.servlet;

import in.hocg.boot.web.SpringContext;
import in.hocg.boot.web.utils.web.RequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Created by hocgin on 2020/8/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class SpringServletContext extends SpringContext {

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

    public static Optional<String> getClientIp() {
        return SpringServletContext.getRequest().map(RequestUtils::getClientIp);
    }
}
