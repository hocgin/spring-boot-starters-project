package in.hocg.sso.client.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * Created by hocgin on 2020/8/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
public class ExampleController {
    @ResponseBody
    @GetMapping("/example")
    public String index(Principal principal) {
        return "ok" + principal;
    }
}
