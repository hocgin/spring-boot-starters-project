package in.hocg.sso2.client.sample.config.authorization;

/**
 * Created by hocgin on 2020/8/29
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
//public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//    @Override
//    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
//        List<String> authorities = Collections.emptyList();
//        return mono.filter(Authentication::isAuthenticated)
//            .flatMapIterable(Authentication::getAuthorities)
//            .map(GrantedAuthority::getAuthority)
//            .any(authorities::contains)
//            .map(AuthorizationDecision::new)
//            .defaultIfEmpty(new AuthorizationDecision(false));
//    }
//}
