package in.hocg.boot.sso.client.autoconfigure.endpoint;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.sso.client.autoconfigure.core.PageConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2021/12/30
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Api(tags = "SSO.Client(应用)")
@Controller
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SsoClientEndpoint {

    @ApiOperation(value = "退出登录")
    @RequestMapping(PageConstants.LOGOUT_URL)
    public String logout() {
        SecurityContextHolder.clearContext();
        return StrUtil.format("redirect:{}", PageConstants.LOGIN_URL);
    }
}
