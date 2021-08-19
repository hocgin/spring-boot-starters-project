package in.hocg.boot.openfeign.autoconfiguration;

import feign.Logger;
import feign.codec.ErrorDecoder;
import in.hocg.boot.openfeign.autoconfiguration.decoder.ExceptionDecoder;
import in.hocg.boot.openfeign.autoconfiguration.interceptor.UserAgentInterceptor;
import in.hocg.boot.openfeign.autoconfiguration.properties.OpenFeignProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableConfigurationProperties({OpenFeignProperties.class})
@ConditionalOnClass(FeignRibbonClientAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class OpenFeignAutoConfiguration {
    private final OpenFeignProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder errorDecoder() {
        return new ExceptionDecoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserAgentInterceptor userAgentInterceptor() {
        return new UserAgentInterceptor(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
