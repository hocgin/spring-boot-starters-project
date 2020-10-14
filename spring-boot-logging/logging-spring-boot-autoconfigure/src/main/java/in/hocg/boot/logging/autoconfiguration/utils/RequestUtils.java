package in.hocg.boot.logging.autoconfiguration.utils;

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
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(ip)
            && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(ip)
            && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 本地名单
        if (Arrays.asList(new String[]{
            "0:0:0:0:0:0:0:1",
            "127.0.0.1"
        }).contains(request.getRemoteAddr())) {
            return "110.80.68.212";
        }
        return request.getRemoteAddr();
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
