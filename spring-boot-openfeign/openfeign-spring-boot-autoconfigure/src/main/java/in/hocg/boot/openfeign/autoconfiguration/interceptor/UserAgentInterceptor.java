package in.hocg.boot.openfeign.autoconfiguration.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import in.hocg.boot.openfeign.autoconfiguration.properties.OpenFeignProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hocgin on 2021/5/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class UserAgentInterceptor implements RequestInterceptor {
    private final OpenFeignProperties properties;
    public static final String USER_AGENT = "X-USERAGENT";

    @Override
    public void apply(RequestTemplate template) {
        template.header(USER_AGENT, properties.getUserAgent());
    }
}
