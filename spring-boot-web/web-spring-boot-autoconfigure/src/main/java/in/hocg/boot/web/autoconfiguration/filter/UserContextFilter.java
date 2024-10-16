package in.hocg.boot.web.autoconfiguration.filter;

import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.utils.context.security.UserDetail;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hocgin on 2023/01/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class UserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            UserContextHolder.setUserDetail(getUserDetail(request, response));
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clearAll();
        }
    }

    public abstract UserDetail getUserDetail(HttpServletRequest request, HttpServletResponse response);
}
