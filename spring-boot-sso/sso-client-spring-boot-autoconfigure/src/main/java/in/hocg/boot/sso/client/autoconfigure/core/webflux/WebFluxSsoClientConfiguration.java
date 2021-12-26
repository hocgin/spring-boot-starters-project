package in.hocg.boot.sso.client.autoconfigure.core.webflux;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.sso.client.autoconfigure.core.AuthenticationResult;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.bearer.ServerBearerTokenAuthenticationConverter;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ApplicationContext context) {
        this.context = context;
        String[] ignoreUrls = properties.getIgnoreUrls().toArray(new String[]{});
        String[] denyUrls = properties.getDenyUrls().toArray(new String[]{});
        String[] authenticatedUrls = properties.getAuthenticatedUrls().toArray(new String[]{});
        Map<String, List<String>> hasAnyRole = properties.getHasAnyRole();
        Map<String, List<String>> hasAnyAuthority = properties.getHasAnyAuthority();
        Map<String, List<String>> hasIpAddress = properties.getHasIpAddress();
        {
            ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec =
                http.authorizeExchange();

            // 如果配置需登陆
            if (authenticatedUrls.length > 0) {
                authorizeExchangeSpec.pathMatchers(authenticatedUrls).authenticated();
            }

            // 如果配置角色
            if (CollUtil.isNotEmpty(hasAnyRole)) {
                hasAnyRole.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && CollUtil.isNotEmpty(entry.getValue()))
                    .forEach(entry -> authorizeExchangeSpec.pathMatchers(entry.getKey()).hasAnyRole(ArrayUtil.toArray(entry.getValue(), String.class)));
            }

            // 如果配置权限
            if (CollUtil.isNotEmpty(hasAnyAuthority)) {
                hasAnyAuthority.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && CollUtil.isNotEmpty(entry.getValue()))
                    .forEach(entry -> authorizeExchangeSpec.pathMatchers(entry.getKey()).hasAnyAuthority(ArrayUtil.toArray(entry.getValue(), String.class)));
            }

            // 如果配置IP白名单
            if (CollUtil.isNotEmpty(hasIpAddress)) {
                hasIpAddress.entrySet().stream()
                    .filter(entry -> StrUtil.isNotBlank(entry.getKey()) && CollUtil.isNotEmpty(entry.getValue()))
                    .forEach(entry -> authorizeExchangeSpec.pathMatchers(entry.getKey()).access((mono, authorizationContext) -> {
                        String ip = Objects.requireNonNull(authorizationContext.getExchange().getRequest().getRemoteAddress()).getAddress().toString().replace("/", "");
                        return mono.map((a) -> new AuthorizationDecision(a.isAuthenticated()))
                            .defaultIfEmpty(new AuthorizationDecision(entry.getValue().contains(ip)));
                    }));
            }

            // 如果配置忽略
            if (ignoreUrls.length > 0) {
                authorizeExchangeSpec.pathMatchers(ignoreUrls).permitAll();
            }

            // 如果配置禁止访问
            if (denyUrls.length > 0) {
                authorizeExchangeSpec.pathMatchers(denyUrls).denyAll();
            }

            authorizeExchangeSpec.anyExchange()
                .authenticated().and();
        }
        http.oauth2Login();
        http.csrf().disable();

        http.exceptionHandling()
            .authenticationEntryPoint(new DelegatingServerAuthenticationEntryPoint(
                getOAuthServerAuthenticationEntryPoint(), getAjaxServerAuthenticationEntryPoint()
            ));

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager(context));
        authenticationWebFilter.setAuthenticationFailureHandler((exchange, exception) -> handleAuthentication4Webflux(exchange.getExchange()));
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter()
            .setAllowUriQueryParameter(true));
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebFluxExpandAuthenticationManager authenticationManager(ApplicationContext applicationContext) {
        return new WebFluxExpandAuthenticationManager(applicationContext);
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
