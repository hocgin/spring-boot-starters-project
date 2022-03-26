package in.hocg.boot.mybatis.plus.extensions.httplog.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import in.hocg.boot.logging.autoconfiguration.core.LoggerEvent;
import in.hocg.boot.logging.autoconfiguration.core.LoggerListener;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Direction;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.service.HttpLogMpeService;
import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class LoggerListenerImpl implements LoggerListener {
    @Lazy
    @Autowired(required = false)
    private HttpLogMpeService httpLogMpeService;

    @Override
    public void handle(LoggerEvent logger) {
        httpLogMpeService.asyncCreate(asCreateLogRo(logger));
    }

    private CreateLogRo asCreateLogRo(LoggerEvent logger) {
        String applicationName = SpringContext.getBootConfig().getSpringApplicationName();
        HashMap<String, Object> header = Maps.newHashMap();
        header.put("host", logger.getHost());
        header.put("user-agent", logger.getUserAgent());

        CreateLogRo result = new CreateLogRo();
        result.setCode(logger.getMapping());
        result.setResponseBody(logger.getRetStr());
        result.setFailReason(logger.getException());
        result.setCreatedAt(logger.getCreatedAt());
        result.setCreator(UserContextHolder.getUserId());
        result.setRequestIp(logger.getClientIp());
        result.setDirection(Direction.In.getCodeStr());
        result.setStatus((StrUtil.isNotBlank(logger.getException()) ? Status.Success : Status.Fail).getCodeStr());
        result.setRemark(Joiner.on(System.lineSeparator()).join(logger.getLogs()));
        result.setDoneAt(logger.getCreatedAt());

        return result
            .setRequestMethod(StrUtil.nullToEmpty(logger.getMethod()).toUpperCase())
            .setAttach(logger.getArgsStr())
            .setRequestHeaders(JSONUtil.toJsonStr(header))
            .setCaller(logger.getSource())
            .setBeCaller(applicationName)
            .setTitle(logger.getEnterRemark())
            .setUri(logger.getUri());
    }
}
