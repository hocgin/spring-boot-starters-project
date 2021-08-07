package in.hocg.boot.http.log.autoconfiguration.core;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class HttpLog {
    private Long id;
    /**
     * 请求路径
     */
    private String uri;
    private String requestMethod;
    /**
     * 请求
     */
    private String requestBody;
    private String requestHeaders;
    /**
     * 响应
     */
    private String responseHeaders;
    private String responseBody;
    /**
     * 名称
     */
    private String title;
    /**
     * 业务编码
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
    /**
     * 扩展数据
     */
    private String attach;
    /**
     * 出入方向 [出、入]
     */
    private String direction;
    /**
     * 响应系统
     */
    private String beCaller;
    /**
     * 请求系统
     */
    private String caller;
    /**
     * 失败原因
     */
    private String failReason;
    private String status;
    private String creator;
    private String creatorIp;
    private LocalDateTime createdAt;
    private LocalDateTime doneAt;
}
