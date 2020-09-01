package in.hocg.sso2.server.sample.config.security.user.authentication.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by hocgin on 2020/1/9.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TokenUtility {
    private static final HashMap<String, Object> EMPTY = new HashMap<>();
    private static final String KEY = "hocgin";

    public static String encode(String subject) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long expirationTimeMillis = currentTimeMillis + (1000 * 60 * 60 * 10);
        return Jwts.builder().setClaims(EMPTY).setSubject(subject)
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
