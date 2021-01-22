package in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface BearerTokenErrorCodes {

    /**
     * {@code invalid_request} - The request is missing a required parameter, includes an unsupported parameter or
     * parameter value, repeats the same parameter, uses more than one method for including an access token, or is
     * otherwise malformed.
     */
    String INVALID_REQUEST = "invalid_request";

    /**
     * {@code invalid_token} - The access token provided is expired, revoked, malformed, or invalid for other
     * reasons.
     */
    String INVALID_TOKEN = "invalid_token";

    /**
     * {@code insufficient_scope} - The request requires higher privileges than provided by the access token.
     */
    String INSUFFICIENT_SCOPE = "insufficient_scope";

}
