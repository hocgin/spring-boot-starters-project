package in.hocg.sso2.server.sample.config.security.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(prefix = SsoProperties.PREFIX)
public class SsoProperties {
    public static final String PREFIX = "sso.oauth";
    private List<Client> clients = Collections.emptyList();

    @Data
    public static class Client {
        private String clientId;
        private String clientSecret;
        private String[] redirectUris;
    }
}
