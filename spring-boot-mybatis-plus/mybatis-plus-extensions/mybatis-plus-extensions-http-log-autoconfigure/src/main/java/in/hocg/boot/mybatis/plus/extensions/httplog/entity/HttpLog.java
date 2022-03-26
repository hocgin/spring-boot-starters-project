package in.hocg.boot.mybatis.plus.extensions.httplog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.GlobalConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(HttpLog.TABLE_NAME)
public class HttpLog extends CommonEntity<HttpLog> {
    public static final String TABLE_NAME = GlobalConstants.TABLE_PREFIX + "http_log";

    @TableField("uri")
    private String uri;
    @TableField("request_method")
    private String requestMethod;
    @TableField("request_body")
    private String requestBody;
    @TableField("request_headers")
    private String requestHeaders;
    @TableField("response_headers")
    private String responseHeaders;
    @TableField("response_body")
    private String responseBody;
    @TableField("title")
    private String title;
    @TableField("code")
    private String code;
    @TableField("remark")
    private String remark;
    @TableField("attach")
    private String attach;
    @TableField("direction")
    private String direction;
    @TableField("be_caller")
    private String beCaller;
    @TableField("caller")
    private String caller;
    @TableField("fail_reason")
    private String failReason;
    @TableField("status")
    private String status;
    @TableField("request_ip")
    private String requestIp;
    @TableField("done_at")
    private LocalDateTime doneAt;
}
