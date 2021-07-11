package in.hocg.sso2.server.sample.config.security.config;

/**
 * Created by hocgin on 2020/1/7.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
//@Configuration
//@EnableResourceServer
//@RequiredArgsConstructor(onConstructor = @__(@Lazy))
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.requestMatcher(this.isAuthorizationRequestMatcher())
//            .authorizeRequests()
//            .anyRequest().permitAll();
//    }
//
//    private RequestMatcher isAuthorizationRequestMatcher() {
//        return request -> {
//            String authorization = request.getHeader(StringPoolUtils.HEADER_AUTHORIZATION);
//            return new AntPathRequestMatcher("/oauth/**").matches(request) ||
//                (Objects.nonNull(authorization) && (StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BEARER)
//                    || StrUtil.startWithIgnoreCase(authorization, StringPoolUtils.HEADER_VALUE_BASIC)));
//        };
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.stateless(true);
//    }
//}
