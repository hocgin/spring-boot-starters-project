package in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.Assert;

/**
 * Created by hocgin on 2021/1/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public final class BearerTokenError extends OAuth2Error {

    private final HttpStatus httpStatus;

    private final String scope;

    /**
     * Create a {@code BearerTokenError} using the provided parameters
     *
     * @param errorCode  the error code
     * @param httpStatus the HTTP status
     */
    public BearerTokenError(String errorCode, HttpStatus httpStatus, String description, String errorUri) {
        this(errorCode, httpStatus, description, errorUri, null);
    }

    /**
     * Create a {@code BearerTokenError} using the provided parameters
     *
     * @param errorCode   the error code
     * @param httpStatus  the HTTP status
     * @param description the description
     * @param errorUri    the URI
     * @param scope       the scope
     */
    public BearerTokenError(String errorCode, HttpStatus httpStatus, String description, String errorUri, String scope) {
        super(errorCode, description, errorUri);
        Assert.notNull(httpStatus, "httpStatus cannot be null");

        Assert.isTrue(isDescriptionValid(description),
            "description contains invalid ASCII characters, it must conform to RFC 6750");
        Assert.isTrue(isErrorCodeValid(errorCode),
            "errorCode contains invalid ASCII characters, it must conform to RFC 6750");
        Assert.isTrue(isErrorUriValid(errorUri),
            "errorUri contains invalid ASCII characters, it must conform to RFC 6750");
        Assert.isTrue(isScopeValid(scope),
            "scope contains invalid ASCII characters, it must conform to RFC 6750");

        this.httpStatus = httpStatus;
        this.scope = scope;
    }

    /**
     * Return the HTTP status.
     *
     * @return the HTTP status
     */
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * Return the scope.
     *
     * @return the scope
     */
    public String getScope() {
        return this.scope;
    }

    private static boolean isDescriptionValid(String description) {
        return description == null ||
            description.chars().allMatch(c ->
                withinTheRangeOf(c, 0x20, 0x21) ||
                    withinTheRangeOf(c, 0x23, 0x5B) ||
                    withinTheRangeOf(c, 0x5D, 0x7E));
    }

    private static boolean isErrorCodeValid(String errorCode) {
        return errorCode.chars().allMatch(c ->
            withinTheRangeOf(c, 0x20, 0x21) ||
                withinTheRangeOf(c, 0x23, 0x5B) ||
                withinTheRangeOf(c, 0x5D, 0x7E));
    }

    private static boolean isErrorUriValid(String errorUri) {
        return errorUri == null ||
            errorUri.chars().allMatch(c ->
                c == 0x21 ||
                    withinTheRangeOf(c, 0x23, 0x5B) ||
                    withinTheRangeOf(c, 0x5D, 0x7E));
    }

    private static boolean isScopeValid(String scope) {
        return scope == null ||
            scope.chars().allMatch(c ->
                withinTheRangeOf(c, 0x20, 0x21) ||
                    withinTheRangeOf(c, 0x23, 0x5B) ||
                    withinTheRangeOf(c, 0x5D, 0x7E));
    }

    private static boolean withinTheRangeOf(int c, int min, int max) {
        return c >= min && c <= max;
    }
}
