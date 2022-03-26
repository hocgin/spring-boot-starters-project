package in.hocg.boot.mybatis.plus.extensions.httplog.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.httplog.entity.HttpLog;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.DoneLogRo;
import in.hocg.boot.utils.LogUtils;
import org.springframework.scheduling.annotation.Async;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface HttpLogMpeService extends AbstractService<HttpLog> {
    /**
     * 异步创建日志
     *
     * @param ro
     * @return
     */
    @Async
    Future<Serializable> asyncCreate(CreateLogRo ro);

    Serializable create(CreateLogRo ro);

    /**
     * 异步完成日志
     *
     * @param ro
     */
    @Async
    void asyncDone(DoneLogRo ro);

    @Async
    void asyncDone(Serializable id, LogUtils.LogStatus logStatus, String body);
}
