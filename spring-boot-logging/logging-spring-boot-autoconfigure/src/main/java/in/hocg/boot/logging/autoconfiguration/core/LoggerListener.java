package in.hocg.boot.logging.autoconfiguration.core;

import org.springframework.context.event.EventListener;

/**
 * Created by hocgin on 2020/2/28.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface LoggerListener {
    /**
     * 处理日志
     *
     * @param logger
     */
    void handle(LoggerEvent logger);
}
