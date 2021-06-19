package in.hocg.boot.youtube.autoconfiguration.properties;

import lombok.Data;
import lombok.experimental.Accessors;
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
@ConfigurationProperties(YoutubeProperties.PREFIX)
public class YoutubeProperties {
    public static final String PREFIX = "boot.youtube";

    /**
     * 客户端配置
     */
    private List<ClientConfig> clients = Collections.emptyList();

    @Data
    @Accessors(chain = true)
    public static class ClientConfig {
        /**
         * 客户id
         */
        private String clientId;
        /**
         * 客户密钥
         */
        private String clientSecret;
        /**
         * 应用名称
         */
        private String applicationName;
    }
}
