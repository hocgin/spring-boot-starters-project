package in.hocg.sso.server.sample.config.security.user;

import in.hocg.boot.web.autoconfiguration.utils.web.RequestUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class IsAjaxRequestMatcher implements RequestMatcher {
    public static final IsAjaxRequestMatcher THIS = new IsAjaxRequestMatcher();

    @Override
    public boolean matches(HttpServletRequest request) {
        return RequestUtils.isAJAX(request);
    }
}
