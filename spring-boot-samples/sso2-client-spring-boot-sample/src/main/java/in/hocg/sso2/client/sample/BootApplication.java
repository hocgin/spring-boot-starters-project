package in.hocg.sso2.client.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;

/**
 * Created by hocgin on 2020/8/15.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @RequestMapping("/user")
    public Principal s(Principal principal) {
        return principal;
    }

    @RequestMapping("/ignore")
    public String ignore(Principal principal) {
        return "是登陆: " + Objects.nonNull(principal);
    }
}
