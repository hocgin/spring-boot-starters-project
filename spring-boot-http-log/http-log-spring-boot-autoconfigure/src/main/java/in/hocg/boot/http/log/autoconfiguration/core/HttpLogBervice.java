package in.hocg.boot.http.log.autoconfiguration.core;

import in.hocg.boot.utils.LogUtils;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface HttpLogBervice {
    <T> void call(SupplierThrow<T> exec, ThreeConsumerThrow<Serializable, LogUtils.LogStatus, String> onComplete);

    <T> void call(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady, ThreeConsumerThrow<Serializable, LogUtils.LogStatus, String> onComplete);

    <T> void call(SupplierThrow<T> exec, SupplierThrow<Serializable> onReady);

    <T> void call(String title, String code, String caller, String beCaller, String uri, Map<String, String> headers, Object body, SupplierThrow<T> exec);

    HttpLog create(HttpLog entity);

    Serializable syncReady(String title, String code, String remark, String attach,
                           String caller, String beCaller, String creator, String direction,
                           String uri, Map<String, String> headers, Object body);
}
