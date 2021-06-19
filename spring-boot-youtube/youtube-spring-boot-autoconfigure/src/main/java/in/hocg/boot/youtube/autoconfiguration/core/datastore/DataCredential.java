package in.hocg.boot.youtube.autoconfiguration.core.datastore;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class DataCredential implements Serializable {
    /**
     * 访问token
     */
    private String accessToken;
    /**
     * 过期时间 ms
     */
    private Long expirationTimeMilliseconds;
    /**
     * 刷新token
     */
    private String refreshToken;
}
