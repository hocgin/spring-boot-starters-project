package in.hocg.boot.http.log.autoconfiguration.core;

import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface HttpLogRepository {

    HttpLog create(HttpLog entity);

    HttpLog create(String title, String code, String remark, String attach,
                   String caller, String beCaller, String creator, String direction,
                   String uri, Map<String, List<String>> headers, Object body);

    void updateById(Long logId, String status, String failReason, String responseBody, String responseHeaders);
}
