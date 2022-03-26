package in.hocg.boot.mybatis.plus.extensions.httplog.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Direction;
import in.hocg.boot.mybatis.plus.extensions.httplog.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.DoneLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.service.HttpLogMpeService;
import in.hocg.boot.utils.LogUtils;
import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.utils.function.SupplierThrow;
import in.hocg.boot.utils.function.ThreeConsumerThrow;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.experimental.UtilityClass;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class HttpLogUtils {

    public HttpLogMpeService getHttpLogMpeService() {
        return SpringContext.getBean(HttpLogMpeService.class);
    }

    public <T> ThreeConsumerThrow<Serializable, LogUtils.LogStatus, T> getDefaultComplete() {
        return (Serializable id, LogUtils.LogStatus status, T result) -> {
            if (Objects.isNull(result) || result instanceof String) {
                getHttpLogMpeService().asyncDone(id, status, StrUtil.toString(result));
            } else if (result instanceof HttpResponse) {
                getHttpLogMpeService().asyncDone(response((HttpResponse) result).setId(Convert.toLong(id)));
            }
        };
    }

    public SupplierThrow<Serializable> getReady(CreateLogRo ro) {
        return () -> getHttpLogMpeService().create(ro);
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


    public static void main(String[] args) {
        HttpRequest request = HttpUtil.createRequest(null, "");
        HttpResponse execute = request.execute();
        HttpResponse result = LogUtils.logSync(request::execute, HttpLogUtils.getReady(HttpLogUtils.request(request)), HttpLogUtils.getDefaultComplete());
    }

}
