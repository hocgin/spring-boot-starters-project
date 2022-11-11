package in.hocg.boot.utils.servlet;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author hocgin
 */
@UtilityClass
public class WebUtils {

    /**
     * 获取请求的 IP
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
}
