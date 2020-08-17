package in.hocg.sso.server.sample.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by hocgin on 2020/1/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class IndexEndpoint {

    @RequestMapping("/signup")
    public ModelAndView signup() {
        return new ModelAndView("signup");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping({"/", "/index.html", "/index"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/user/me")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }

}
