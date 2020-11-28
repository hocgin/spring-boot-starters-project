package in.hocg.boot.sso.client.autoconfigure.core.webflux;

import in.hocg.boot.sso.client.autoconfigure.core.AuthenticationResult;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2020/9/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxSsoClientConfiguration {
    private final SsoClientProperties properties;
    private ApplicationContext context;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ApplicationContext context) {
        this.context = context;
        String[] ignoreUrls = properties.getIgnoreUrls().toArray(new String[]{});
        {
            ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec =
                http.authorizeExchange();
            if (ignoreUrls.length > 0) {
                authorizeExchangeSpec.pathMatchers(ignoreUrls).permitAll();
            }
            authorizeExchangeSpec
                .anyExchange()
                .authenticated().and();
        }
        http.oauth2Login();
        http.csrf().disable();


//        http.exceptionHandling()
//            .authenticationEntryPoint(new DelegatingServerAuthenticationEntryPoint(
//                getOAuthServerAuthenticationEntryPoint(), getAjaxServerAuthenticationEntryPoint()
//            ));
        return http.build();
    }

    private DelegatingServerAuthenticationEntryPoint.DelegateEntry getAjaxServerAuthenticationEntryPoint() {
        return new DelegatingServerAuthenticationEntryPoint.DelegateEntry(exchange -> {
            if (Objects.equals(exchange.getRequest().getHeaders().getFirst("X-Requested-With"), "XMLHttpRequest")) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
            return ServerWebExchangeMatcher.MatchResult.notMatch();
        }, (exchange, e) -> this.handleAuthentication4Webflux(exchange));
    }


    private Mono<Void> handleAuthentication4Webflux(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();

        String redirectUrl = null;
        String xPageUrl = headers.getFirst("X-Page-Url");
        if (StringUtils.isEmpty(xPageUrl)) {
            xPageUrl = headers.getFirst("Referer");
        }

        if (!StringUtils.isEmpty(xPageUrl)) {
            redirectUrl = xPageUrl;
        }

        AuthenticationResult result = AuthenticationResult.create(redirectUrl);
        DataBuffer buffer = response.bufferFactory()
            .wrap(result.toJSON().getBytes(StandardCharsets.UTF_8));
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(buffer));
    }

    private DelegatingServerAuthenticationEntryPoint.DelegateEntry getOAuthServerAuthenticationEntryPoint() {
        MediaTypeServerWebExchangeMatcher htmlMatcher = new MediaTypeServerWebExchangeMatcher(
            MediaType.TEXT_HTML);
        htmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        Map<String, String> urlToText = this.getLinks();
        String authenticationEntryPointRedirectPath;
        if (urlToText.size() == 1) {
            authenticationEntryPointRedirectPath = urlToText.keySet().iterator().next();
        } else {
            authenticationEntryPointRedirectPath = "/login";
        }
        RedirectServerAuthenticationEntryPoint serverAuthenticationEntryPoint = new RedirectServerAuthenticationEntryPoint(authenticationEntryPointRedirectPath);
        return new DelegatingServerAuthenticationEntryPoint.DelegateEntry(htmlMatcher, serverAuthenticationEntryPoint);
    }

    private Map<String, String> getLinks() {
        Iterable<ClientRegistration> registrations = getBeanOrNull(ResolvableType.forClassWithGenerics(Iterable.class, ClientRegistration.class));
        if (registrations == null) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        registrations.iterator().forEachRemaining(r -> result.put("/oauth2/authorization/" + r.getRegistrationId(), r.getClientName()));
        return result;
    }

    private <T> T getBeanOrNull(ResolvableType type) {
        if (this.context == null) {
            return null;
        }
        String[] names = this.context.getBeanNamesForType(type);
        if (names.length == 1) {
            return (T) this.context.getBean(names[0]);
        }
        return null;
    }
}
