package in.hocg.boot.youtube.autoconfiguration.utils.data;

import com.google.api.client.auth.oauth2.Credential;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/3/30
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class CredentialChannel {
    @ApiModelProperty("频道信息")
    private YouTubeChannel channel;
    @ApiModelProperty("凭据")
    private Credential credential;
}
