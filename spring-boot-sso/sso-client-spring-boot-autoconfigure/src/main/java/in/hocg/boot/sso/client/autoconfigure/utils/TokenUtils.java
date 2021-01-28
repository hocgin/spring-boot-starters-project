package in.hocg.boot.sso.client.autoconfigure.utils;

import in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer.BearerTokenError;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer.BearerTokenErrorCodes;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TokenUtils {
    public static final AnonymousAuthenticationToken ANONYMOUS_AUTHENTICATION_TOKEN = new AnonymousAuthenticationToken("key", "anonymous",
        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");

    /**
     * 解析 Bearer {token}
     *
     * @param authorization
     * @return
     */
    public static String resolveFromAuthorizationHeader(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorization);

            if (!matcher.matches()) {
                BearerTokenError error = new BearerTokenError(BearerTokenErrorCodes.INVALID_TOKEN,
                    HttpStatus.BAD_REQUEST,
                    "Bearer token is malformed",
                    "https://tools.ietf.org/html/rfc6750#section-3.1");
                throw new OAuth2AuthenticationException(error);
            }

            return matcher.group("token");
        }
        return null;
    }
}
