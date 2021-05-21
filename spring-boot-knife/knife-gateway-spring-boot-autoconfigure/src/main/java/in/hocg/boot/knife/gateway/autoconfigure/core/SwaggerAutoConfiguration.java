package in.hocg.boot.knife.gateway.autoconfigure.core;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by hocgin on 2020/8/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
public class SwaggerAutoConfiguration {

    @Bean
    public SwaggerHeaderFilter swaggerHeaderFilter() {
        return new SwaggerHeaderFilter();
    }

    @Bean
    @Primary
    public DefaultSwaggerResourcesProvider swaggerResourcesProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        return new DefaultSwaggerResourcesProvider(routeLocator, gatewayProperties);
    }
}
