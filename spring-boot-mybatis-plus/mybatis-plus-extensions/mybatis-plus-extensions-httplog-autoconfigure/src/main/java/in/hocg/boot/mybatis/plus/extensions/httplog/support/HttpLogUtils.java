package in.hocg.boot.mybatis.plus.extensions.httplog.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.google.common.collect.Maps;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Direction;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.DoneLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.service.HttpLogMpeService;
import in.hocg.boot.utils.utils.LogUtils;
import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.web.autoconfiguration.utils.SerializableUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class HttpLogUtils {

    public HttpLogMpeService getHttpLogMpeService() {
        return SpringContext.getBean(HttpLogMpeService.class);
    }

    public <T> ThreeConsumerThrow<Serializable, LogUtils.LogStatus, T> getDefaultComplete() {
        return getDefaultComplete(null);
    }

    public <T> ThreeConsumerThrow<Serializable, LogUtils.LogStatus, T> getDefaultComplete(ThreeConsumerThrow<Serializable, LogUtils.LogStatus, T> complete) {
        return (Serializable id, LogUtils.LogStatus status, T result) -> {
            if (LogUtils.LogStatus.Fail.equals(status)) {
                getHttpLogMpeService().asyncFail(id, result);
            } else if (result instanceof HttpResponse) {
                getHttpLogMpeService().asyncDone(response((HttpResponse) result).setId(Convert.toLong(id)));
            }
            // 兜底
            else {
                getHttpLogMpeService().asyncDone(id, status, StrUtil.toString(result));
            }
            if (Objects.nonNull(complete)) {
                try {
                    complete.accept(id, status, result);
                } catch (Exception e) {
                    log.error("执行补充操作失败", e);
                }
            }
        };
    }

    public SupplierThrow<Serializable> getReady(CreateLogRo ro) {
        return () -> getHttpLogMpeService().create(ro);
    }

    public SupplierThrow<Future<Serializable>> getAsyncReady(CreateLogRo ro) {
        return getAsyncReady(() -> ro);
    }

    public SupplierThrow<Future<Serializable>> getAsyncReady(Callable<CreateLogRo> getCreateLogRo) {
        return () -> getHttpLogMpeService().asyncCreate(getCreateLogRo.call());
    }

    public CreateLogRo request(HttpRequest request) {
        return request(request, null, null, null);
    }

    public CreateLogRo request(HttpRequest request, String beCaller, String title) {
        return request(request, beCaller, null, title);
    }

    public CreateLogRo request(HttpRequest request, String beCaller, String code, String title) {
        String applicationName = SpringContext.getBootConfig().getSpringApplicationName();
        CreateLogRo result = new CreateLogRo();
        result.setCreatedAt(LocalDateTime.now());
        result.setCreator(UserContextHolder.getUserId());
        result.setCode(code);

        // form 表单
        Map<String, Object> form = ObjectUtil.defaultIfNull(request.form(), Collections.emptyMap());
        if (!form.isEmpty()) {
            Map<String, Object> params = Maps.newHashMap();
            form.forEach((k, v) -> params.put(k, SerializableUtils.toStr(v)));
            result.setRequestBody(JSONUtil.toJsonStr(params));
        }
        // body
        else {
            result.setRequestBody(StrUtil.str(ReflectUtil.getFieldValue(request, "bodyBytes"), request.charset()));
        }

        result.setRequestIp(SystemUtil.getHostInfo().getAddress());
        result.setDirection(Direction.Out.getCodeStr());
        result.setStatus(Status.Executing.getCodeStr());

        return result
            .setRequestMethod(request.getMethod().name().toUpperCase())
            .setRequestHeaders(JSONUtil.toJsonStr(request.headers()))
            .setCaller(applicationName)
            .setBeCaller(beCaller)
            .setTitle(title)
            .setUri(request.getUrl());
    }

    public DoneLogRo response(HttpResponse response) {
        boolean isSuccess = response.getStatus() == 200;
        DoneLogRo result = new DoneLogRo();

        result.setDoneAt(LocalDateTime.now());
        if (!isSuccess) {
            result.setFailReason(StrUtil.format("状态码: {}", response.getStatus()));
        }
        result.setResponseBody(response.body());
        result.setResponseHeaders(JSONUtil.toJsonStr(response.headers()));
        result.setStatus((isSuccess ? Status.Success : Status.Fail).getCodeStr());
        return result;
    }

}
