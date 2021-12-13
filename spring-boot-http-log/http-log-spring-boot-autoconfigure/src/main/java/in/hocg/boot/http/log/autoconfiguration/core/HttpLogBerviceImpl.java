package in.hocg.boot.http.log.autoconfiguration.core;

import in.hocg.boot.http.log.autoconfiguration.jdbc.TableHttpLog;
import in.hocg.boot.utils.LogUtils;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class HttpLogBerviceImpl implements HttpLogBervice {
    @Lazy
    @Autowired(required = false)
    private HttpLogRepository repository;
    @Lazy
    @Autowired(required = false)
    private HttpLogBervice self;

    @Override
    public <T> T asyncCall(SupplierThrow<T> exec, SupplierThrow<Future<Serializable>> onReady) {
        return asyncCall(exec, onReady, self::asyncComplete);
    }

    @Override
    public <T> T asyncCall(SupplierThrow<T> exec, SupplierThrow<Future<Serializable>> onReady, ThreeConsumerThrow<Serializable, LogUtils.LogStatus, String> onComplete) {
        return LogUtils.logAsync(exec, onReady, onComplete);
    }

    @Override
    public <T> T syncCall(SupplierThrow<T> exec, ThreeConsumerThrow<Serializable, LogUtils.LogStatus, String> onComplete) {
        return syncCall(exec, null, onComplete);
    }

    @Override
    public <T> T syncCall(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady) {
        return syncCall(exec, onReady, self::asyncComplete);
    }

    @Override
    public <T> T syncCall(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady, ThreeConsumerThrow<Serializable, LogUtils.LogStatus, String> onComplete) {
        return LogUtils.logSync(exec, onReady, onComplete);
    }

    @Override
    public <T> T syncCall(String title, String code, String caller, String beCaller, String uri, Map<String, String> headers, Object body, SupplierThrow<T> exec) {
        return this.syncCall(exec, () -> this.syncReady(title, code, null, null, caller, beCaller, null, TableHttpLog.Direction.Out.getCodeStr(), uri, headers, body));
    }

    @Override
    public HttpLog create(HttpLog entity) {
        return repository.create(entity);
    }

    @Override
    @SneakyThrows
    public Serializable syncReady(String title, String code, String remark, String attach, String caller, String beCaller, String creator, String direction, String uri, Map<String, String> headers, Object body) {
        return asyncReady(title, code, remark, attach, caller, beCaller, creator, direction, uri, headers, body).get();
    }

    @Override
    public Future<Serializable> asyncReady(String title, String code, String remark, String attach, String caller, String beCaller, String creator, String direction, String uri, Map<String, String> headers, Object body) {
        return AsyncResult.forValue(repository.create(title, code, remark, attach, caller, beCaller, creator, direction, uri, headers, body).getId());
    }

    @Override
    public void asyncComplete(Serializable logId, LogUtils.LogStatus status, String result) {
        if (LogUtils.LogStatus.Success.equals(status)) {
            repository.updateById(((Long) logId), TableHttpLog.Status.Success.getCodeStr(), null, result, null);
        } else if (LogUtils.LogStatus.Fail.equals(status)) {
            repository.updateById(((Long) logId), TableHttpLog.Status.Fail.getCodeStr(), result, null, null);
        } else {
            log.warn("无法更新 HTTP 日志: [{}, {}]", logId, status);
        }
    }

}
