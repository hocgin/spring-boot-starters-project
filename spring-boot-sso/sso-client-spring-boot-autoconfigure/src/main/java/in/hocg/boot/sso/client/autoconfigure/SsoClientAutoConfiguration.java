package in.hocg.boot.sso.client.autoconfigure;

import in.hocg.boot.sso.client.autoconfigure.core.servlet.ServletSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.core.webflux.WebFluxSsoClientConfiguration;
import in.hocg.boot.sso.client.autoconfigure.properties.SsoClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SsoClientProperties.class)
@Import({ServletSsoClientConfiguration.class, WebFluxSsoClientConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SsoClientAutoConfiguration {
    private final SsoClientProperties properties;

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate lbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }


}
