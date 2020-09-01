package in.hocg.sso2.server.sample.config.security.user.authentication.token;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class TokenQo {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
