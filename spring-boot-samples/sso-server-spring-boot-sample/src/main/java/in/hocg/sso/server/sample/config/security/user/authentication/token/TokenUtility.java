package in.hocg.sso.server.sample.config.security.user.authentication.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TokenUtility {
    private static final String KEY = "hocgin";

    public static String encode(String subject) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long expirationTimeMillis = currentTimeMillis + (1000 * 60 * 60 * 10);
        return Jwts.builder().setClaims(Collections.emptyMap()).setSubject(subject)
            .setIssuedAt(new Date(currentTimeMillis))
            .setExpiration(new Date(expirationTimeMillis))
            .signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    public static String decode(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

}
