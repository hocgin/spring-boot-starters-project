package in.hocg.boot.ws.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(WebSocketProperties.PREFIX)
public class WebSocketProperties {
    public static final String PREFIX = "boot.websocket";

    /**
     * websocket 地址
     */
    private List<String> endpoint = Collections.singletonList("/ws");
    /**
     * 允许的源
     */
    private List<String> allowedOrigins = Collections.singletonList("*");
    /**
     * destination
     */
    private List<String> destinationPrefix = Collections.singletonList("/topic");
    /**
     * user destination
     */
    private String userDestinationPrefix = "/user";
    /**
     * app destination
     */
    private String appDestinationPrefix = "/app";

    /**
     * 忽略的路径(支持表达式) -- 暂未启用
     */
    private List<String> ignoreUrls = Collections.emptyList();
    /**
     * 拒绝访问(支持表达式) -- 暂未启用
     */
    private List<String> denyUrls = Collections.emptyList();
    /**
     * 需认证(支持表达式) -- 暂未启用
     */
    private List<String> authenticatedUrls = Collections.emptyList();
}
