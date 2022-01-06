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
@ConfigurationProperties(WsProperties.PREFIX)
public class WsProperties {
    public static final String PREFIX = "boot.websocket";
    /**
     * 忽略的路径(支持表达式)
     */
    private List<String> ignoreUrls = Collections.emptyList();
    /**
     * 拒绝访问(支持表达式)
     */
    private List<String> denyUrls = Collections.emptyList();
    /**
     * 需认证(支持表达式)
     */
    private List<String> authenticatedUrls = Collections.emptyList();
}
