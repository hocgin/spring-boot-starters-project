package in.hocg.sso2.server.sample.config.security.user.authentication.token;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TokenAuthenticationEndpoint {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public String authenticate(@Valid @RequestBody TokenQo qo) {
        final String username = qo.getUsername();
        final String password = qo.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new RuntimeException("用户名或密码错误");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = TokenUtility.encode(userDetails.getUsername());
        return token;
    }
}
