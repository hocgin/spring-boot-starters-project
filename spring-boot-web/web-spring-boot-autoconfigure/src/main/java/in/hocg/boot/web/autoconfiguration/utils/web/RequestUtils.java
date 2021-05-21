package in.hocg.boot.web.autoconfiguration.utils.web;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by hocgin on 2018/9/4.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class RequestUtils {
    /**
     * 获取客户端真实IP
     *
     * @param request
     * @return
     */
    public String getClientIp(HttpServletRequest request) {
        String ip;
        String forwardedFor = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(forwardedFor)
            && !"unknown".equalsIgnoreCase(forwardedFor)) {
            int index = forwardedFor.indexOf(",");
            if (index != -1) {
                ip = forwardedFor.substring(0, index);
            } else {
                ip = forwardedFor;
            }
        } else if (StrUtil.isNotBlank(realIp)
            && !"unknown".equalsIgnoreCase(realIp)) {
            ip = realIp;
        } else {
            ip = request.getRemoteAddr();
        }

        // 本地名单
        if (Arrays.asList(new String[]{"0:0:0:0:0:0:0:1", "127.0.0.1"}).contains(ip)) {
            return "110.80.68.212";
        }
        return ip;
    }

    /**
     * 获取 User-Agent
     * User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36
     *
     * @param request
     * @return
     */
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public String getHost(HttpServletRequest request) {
        return request.getHeader("Host");
    }

    public boolean isAJAX(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
