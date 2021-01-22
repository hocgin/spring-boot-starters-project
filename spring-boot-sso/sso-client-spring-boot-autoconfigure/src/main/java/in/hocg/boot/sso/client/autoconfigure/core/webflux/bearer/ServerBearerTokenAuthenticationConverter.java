package in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer;

import in.hocg.boot.sso.client.autoconfigure.utils.TokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ServerBearerTokenAuthenticationConverter implements ServerAuthenticationConverter {

    private boolean allowUriQueryParameter = false;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(this.token(exchange.getRequest()))
            .map(BearerTokenAuthenticationToken::new);
    }

    private String token(ServerHttpRequest request) {
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
        String parameterToken = request.getQueryParams().getFirst("access_token");
        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                BearerTokenError error = new BearerTokenError(BearerTokenErrorCodes.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Found multiple bearer tokens in the request",
                    "https://tools.ietf.org/html/rfc6750#section-3.1");
                throw new OAuth2AuthenticationException(error);
            }
            return authorizationHeaderToken;
        }
        else if (parameterToken != null && isParameterTokenSupportedForRequest(request)) {
            return parameterToken;
        }
        return null;
    }

    public ServerBearerTokenAuthenticationConverter setAllowUriQueryParameter(boolean allowUriQueryParameter) {
        this.allowUriQueryParameter = allowUriQueryParameter;
        return this;
    }

    private static String resolveFromAuthorizationHeader(HttpHeaders headers) {
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return TokenUtils.resolveFromAuthorizationHeader(authorization);
    }

    private boolean isParameterTokenSupportedForRequest(ServerHttpRequest request) {
        return this.allowUriQueryParameter && HttpMethod.GET.equals(request.getMethod());
    }
}
