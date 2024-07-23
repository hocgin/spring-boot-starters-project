package in.hocg.boot.web.autoconfiguration.filter;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.StringPoolUtils;
import in.hocg.boot.utils.context.TenantContextHolder;
import in.hocg.boot.utils.context.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            UserContextHolder.setTrackId(request.getHeader(StringPoolUtils.TRACK_ID));
            putMDCThrow(StringPoolUtils.TENANT_ID, StrUtil.toString(TenantContextHolder.getTenantId()));
            filterChain.doFilter(request, response);
        } finally {
            removeMDCThrow(StringPoolUtils.TENANT_ID);
            UserContextHolder.clearTrackId();
        }
    }

    private void putMDCThrow(String key, String value) {
        try {
            MDC.put(key, value);
        } catch (Exception e) {
            log.warn("put MDC error", e);
        }
    }

    private void removeMDCThrow(String key) {
        try {
            MDC.remove(key);
        } catch (Exception e) {
            log.warn("remove MDC error", e);
        }
    }
}
