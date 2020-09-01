package in.hocg.sso2.server.sample.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by hocgin on 2020/1/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
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

    @RequestMapping("/oauth/user")
    @ResponseBody
    public Principal user(HttpServletRequest request, Principal principal,
                          @AuthenticationPrincipal UserDetails user) {
        Object object = SecurityContextHolder.getContext().getAuthentication();
        log.info("object [{}], principal [{}], user [{}]", object, principal, user);
        return principal;
    }

    @RequestMapping("/user/me")
    @ResponseBody
    public Principal user(Principal principal) {
        return principal;
    }

}
