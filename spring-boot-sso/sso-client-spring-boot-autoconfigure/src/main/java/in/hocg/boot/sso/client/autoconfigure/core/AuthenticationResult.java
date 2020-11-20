package in.hocg.boot.sso.client.autoconfigure.core;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/11/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class AuthenticationResult implements Serializable {
    private String redirectUrl;

    public static AuthenticationResult create(String redirectUrl) {
        return new AuthenticationResult().setRedirectUrl(redirectUrl);
    }

    public String toJSON() {
        return JSONUtil.toJsonStr(this);
    }
}
