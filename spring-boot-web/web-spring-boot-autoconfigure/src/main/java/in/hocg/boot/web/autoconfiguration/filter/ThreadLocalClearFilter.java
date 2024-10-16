package in.hocg.boot.web.autoconfiguration.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import in.hocg.boot.utils.ThreadLocalClear;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;


@Slf4j
public class ThreadLocalClearFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private Collection<ThreadLocalClear> threadLocalClears;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            filterChain.doFilter(request, response);
        } finally {
            threadLocalClears.forEach(threadLocalClear -> {
                try {
                    threadLocalClear.clear();
                } catch (Exception e) {
                    log.error("请求结束清理会话异常", e);
                }
            });
        }
    }

    @EventListener
    public void init(ApplicationReadyEvent event) {
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        Map<String, ThreadLocalClear> autoClearMap = applicationContext.getBeansOfType(ThreadLocalClear.class);
        if (MapUtil.isNotEmpty(autoClearMap)) {
            threadLocalClears = autoClearMap.values();
        }
    }
}
