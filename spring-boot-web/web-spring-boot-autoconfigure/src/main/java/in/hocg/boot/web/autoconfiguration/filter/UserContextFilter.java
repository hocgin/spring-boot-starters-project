package in.hocg.boot.web.autoconfiguration.filter;

import in.hocg.boot.utils.context.UserContextHolder;
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
        UserContextHolder.setUserId(getUserId(request, response));
        filterChain.doFilter(request, response);
        UserContextHolder.clear();
    }

    public abstract Long getUserId(HttpServletRequest request, HttpServletResponse response);
}
