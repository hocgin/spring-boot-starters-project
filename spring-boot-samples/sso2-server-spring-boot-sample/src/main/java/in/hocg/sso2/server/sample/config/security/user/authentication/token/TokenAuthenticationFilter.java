package in.hocg.sso2.server.sample.config.security.user.authentication.token;

import in.hocg.boot.web.SpringContext;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hocgin on 2020/1/8.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";
    private static final String TOKEN_HEADER = "Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(TOKEN_HEADER);

        if (Strings.isBlank(authorizationHeader) || !authorizationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authorization = authorizationHeader.substring(BEARER.length());

        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = TokenUtility.decode(authorization);
            final UserDetailsService userDetailsService = SpringContext.getBean(UserDetailsService.class);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(token);
        } catch (Exception ignored) {
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
