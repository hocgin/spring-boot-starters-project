package in.hocg.boot.knife.gateway.autoconfigure;

import in.hocg.boot.knife.gateway.DefaultSwaggerResourcesProvider;
import in.hocg.boot.knife.gateway.SwaggerEndpoint;
import in.hocg.boot.knife.gateway.SwaggerHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableConfigurationProperties(KnifeGatewayProperties.class)
@ConditionalOnProperty(prefix = KnifeGatewayProperties.PREFIX, name = "enabled", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class KnifeGatewayAutoConfiguration {
    private final KnifeGatewayProperties properties;

    @Bean
    public SwaggerHeaderFilter swaggerHeaderFilter() {
        return new SwaggerHeaderFilter();
    }

    @Bean
    @Primary
    public DefaultSwaggerResourcesProvider swaggerResourcesProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        return new DefaultSwaggerResourcesProvider(routeLocator, gatewayProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SwaggerEndpoint.class)
    public SwaggerEndpoint swaggerEndpoint(SwaggerResourcesProvider swaggerResources) {
        return new SwaggerEndpoint(swaggerResources);
    }

}
