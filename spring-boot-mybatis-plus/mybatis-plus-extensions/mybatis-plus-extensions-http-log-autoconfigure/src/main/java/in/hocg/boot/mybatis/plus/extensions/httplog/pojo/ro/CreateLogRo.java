package in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class CreateLogRo extends DoneLogRo {
    private String uri;
    private String requestMethod;
    private String requestBody;
    private String requestHeaders;
    private String title;
    private String code;
    private String remark;
    private String attach;
    private String direction;
    private String beCaller;
    private String caller;
    private String requestIp;
    private String ipAddress;

    private LocalDateTime createdAt;
    private Long creator;
}
