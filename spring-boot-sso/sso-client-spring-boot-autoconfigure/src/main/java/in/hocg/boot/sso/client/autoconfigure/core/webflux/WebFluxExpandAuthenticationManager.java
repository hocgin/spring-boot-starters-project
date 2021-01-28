package in.hocg.boot.sso.client.autoconfigure.core.webflux;

import in.hocg.boot.sso.client.autoconfigure.core.BearerTokenAuthentication;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer.BearerTokenAuthenticationToken;
import in.hocg.boot.sso.client.autoconfigure.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class WebFluxExpandAuthenticationManager implements ReactiveAuthenticationManager {
    private final ApplicationContext context;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
            .filter(a -> a instanceof BearerTokenAuthenticationToken)
            .cast(BearerTokenAuthenticationToken.class)
            .map(BearerTokenAuthenticationToken::getToken)
            .flatMap((accessToken -> {
                BearerTokenAuthentication tokenAuthentication = context.getBean(BearerTokenAuthentication.class);
                Authentication authenticationToken = tokenAuthentication.authentication(accessToken);
                if (Objects.isNull(authenticationToken)) {
                    return Mono.just(TokenUtils.ANONYMOUS_AUTHENTICATION_TOKEN);
                }
                return Mono.just(authenticationToken);
            }))
            .cast(Authentication.class);
    }
}
