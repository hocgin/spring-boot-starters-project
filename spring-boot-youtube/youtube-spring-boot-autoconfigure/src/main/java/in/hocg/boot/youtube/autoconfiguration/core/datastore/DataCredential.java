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
    private String accessToken;

    private Long expirationTimeMilliseconds;

    private String refreshToken;
}
