package in.hocg.boot.ws.autoconfiguration.properties;

import com.google.common.collect.Lists;
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
     * 广播前缀，即客户端要订阅的地址 @SendTo("/queue/all") -使用> client.subscribe("/queue/all", handler)
     */
    private List<String> destinationPrefix = Lists.newArrayList("/topic", "/queue");
    /**
     * 对点前缀, 即客户端要订阅的地址 @SendToUser(destinations = destinationPrefix + "/errors") -使用> client.subscribe('/user/{destinationPrefix}/errors', handler);
     */
    private String userDestinationPrefix = "/user";
    /**
     * 应用前缀，即客户端要发送到的目标地址 @MessageMapping("/index") -使用> client.send('/app/index', {});
     */
    private String appDestinationPrefix = "/app";
    /**
     * withSockJS
     */
    private Boolean withSockJS = true;

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
