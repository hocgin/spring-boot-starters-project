package in.hocg.boot.mybatis.plus.extensions.httplog.service.impl;

import cn.hutool.core.convert.Convert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.httplog.convert.HttpLogMpeConvert;
import in.hocg.boot.mybatis.plus.extensions.httplog.entity.HttpLog;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.httplog.mapper.HttpLogMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.DoneLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.service.HttpLogMpeService;
import in.hocg.boot.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class HttpLogMpeServiceImpl extends AbstractServiceImpl<HttpLogMpeMapper, HttpLog>
    implements HttpLogMpeService {
    private final HttpLogMpeConvert convert;

    @Override
    public Future<Serializable> asyncCreate(CreateLogRo ro) {
        HttpLog entity = convert.asHttpLog(ro);
        save(entity);
        return AsyncResult.forValue(entity.getId());
    }

    @Override
    public Serializable create(CreateLogRo ro) {
        HttpLog entity = convert.asHttpLog(ro);
        save(entity);
        return entity.getId();
    }

    @Override
    public void asyncDone(DoneLogRo ro) {
        HttpLog entity = convert.asHttpLog(ro);
        updateById(entity);
    }

    @Override
    public void asyncDone(Serializable id, LogUtils.LogStatus logStatus, String body) {
        boolean isOk = LogUtils.LogStatus.Success.equals(logStatus);
        DoneLogRo doneLogRo = new DoneLogRo().setId(Convert.toLong(id))
            .setStatus((isOk ? Status.Success : Status.Fail).getCodeStr())
            .setDoneAt(LocalDateTime.now());
        if (!isOk) {
            doneLogRo.setFailReason(body);
        } else {
            doneLogRo.setResponseBody(body);
        }
        asyncDone(doneLogRo);
    }
}
