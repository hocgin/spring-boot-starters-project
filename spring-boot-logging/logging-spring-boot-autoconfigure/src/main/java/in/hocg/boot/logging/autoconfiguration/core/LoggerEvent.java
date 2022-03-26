package in.hocg.boot.logging.autoconfiguration.core;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2020/3/3.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class LoggerEvent {
    /**
     * 代码位置
     */
    private String mapping;
    /**
     * 来源(需使用请求参数, 如: source=eagle)
     */
    private String source;
    /**
     * 请求头:host
     */
    private String host;
    /**
     * 请求头:user-agent
     */
    private String userAgent;
    /**
     * 登录账号
     */
    private Object currentUser;
    /**
     * 请求IP
     */
    private String clientIp;
    /**
     * 入口描述
     */
    private String enterRemark;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求URL
     */
    private String uri;
    /**
     * 请求参数
     */
    private List<Object> args;
    /**
     * 响应
     */
    private Object ret;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 请求耗时
     */
    private Long totalTimeMillis;
    /**
     * 发起请求的时间
     */
    private LocalDateTime createdAt;
    /**
     * 线程内日志
     */
    private static final ThreadLocal<List<String>> LOGS_REMARK = new ThreadLocal<>();

    public List<String> getLogs() {
        return LOGS_REMARK.get();
    }


    public static void log(String message) {
        getOrCreateLogPool().add(message);
    }

    private static List<String> getOrCreateLogPool() {
        List<String> list = LOGS_REMARK.get();
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
            LOGS_REMARK.set(list);
        }
        return list;
    }

    public String getArgsStr() {
        return this.toJsonPrettyStr(args);
    }

    public String getRetStr() {
        String retStr;
        if ((ret instanceof InputStream) || (ret instanceof OutputStream)) {
            retStr = "[\"File Stream\"]";
        } else if (ret instanceof ResponseEntity && ((ResponseEntity<?>) ret).getBody() instanceof Resource) {
            retStr = "[\"Resource Stream\"]";
        } else if (Objects.isNull(ret)) {
            retStr = "[\"void\"]";
        } else {
            retStr = this.toJsonPrettyStr(ret);
        }
        return retStr;
    }

    private String toJsonPrettyStr(Object object) {
        if (Objects.isNull(object)) {
            return "null";
        }

        try {
            return JSONUtil.toJsonPrettyStr(object);
        } catch (Exception ignored) {
            return String.valueOf(object);
        }
    }

}
