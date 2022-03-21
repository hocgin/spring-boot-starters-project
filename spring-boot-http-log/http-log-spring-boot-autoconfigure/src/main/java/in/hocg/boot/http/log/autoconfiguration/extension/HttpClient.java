package in.hocg.boot.http.log.autoconfiguration.extension;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import in.hocg.boot.http.log.autoconfiguration.core.HttpLogBervice;
import in.hocg.boot.http.log.autoconfiguration.jdbc.TableHttpLog;
import in.hocg.boot.utils.context.UserContextHolder;
import in.hocg.boot.web.autoconfiguration.SpringContext;

import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2022/3/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class HttpClient {

    public void request(HttpRequest request) {

        String url = request.getUrl();
        Map<String, List<String>> headers = request.headers();
        String title = "";
        String code = "";
        String remark = "";
        String attach = "";
        String caller = "";
        String beCaller = "";


        HttpLogBervice service = SpringContext.getBean(HttpLogBervice.class);
        service.asyncCall(() -> request.execute().body(),
            () -> service.asyncReady(title, code, remark, attach, caller, beCaller,
                Convert.toStr(UserContextHolder.getUserId()), TableHttpLog.Direction.Out.getCodeStr(), url, headers, ""));

    }

}
