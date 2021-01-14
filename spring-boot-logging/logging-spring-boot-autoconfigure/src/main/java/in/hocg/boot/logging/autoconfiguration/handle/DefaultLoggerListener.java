package in.hocg.boot.logging.autoconfiguration.handle;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.logging.autoconfiguration.core.LoggerEvent;
import in.hocg.boot.logging.autoconfiguration.core.LoggerListener;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by hocgin on 2020/2/28.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class DefaultLoggerListener implements LoggerListener {

    @Override
    public void handle(LoggerEvent logger) {
        printlnPrettyLogger(logger);
    }

    private void printlnPrettyLogger(LoggerEvent logger) {
        StringJoiner stringJoiner = new StringJoiner("\n")
            .add("")
            .add("╔═[{}]═{}════════════════════════════════════════════════")
            .add("║ {}")
            .add("║ > {} ({})")
            .add("╠═[请求体]════════════════════════════════════════════════════════════════════════════")
            .add("║ {}")
            .add("╠═[响应体]════════════════════════════════════════════════════════════════════════════")
            .add("║ {}")
            .add("╚═[总耗时:{} ms]══════════════════════════════════════════════════════════════════════")
            .add("");
        final String retStr = logger.getRetStr();
        final String argStr = logger.getArgsStr();

        log.info(stringJoiner.toString(),
            DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_MS_PATTERN),
            getUserStringThrow(logger.getCurrentUser()),
            String.format("%s %s", logger.getMethod(), logger.getUri()),
            logger.getEnterRemark(),
            logger.getMapping(),
            argStr.replaceAll("\n", "\n║ "),
            retStr.replaceAll("\n", "\n║ "),
            logger.getTotalTimeMillis());
    }

    private String getUserStringThrow(Object user) {
        try {
            return getUserString(user);
        } catch (Exception e) {
            log.error("获取用户信息错误", e);
            return "数据异常";
        }
    }

    private String getUserString(Object user) {
        if (Objects.nonNull(user)) {
            return String.format("[@%s]", StrUtil.toString(user));
        }
        return "未登录";
    }
}
