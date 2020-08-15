package in.hocg.boot.knife.gateway.autoconfigure;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import in.hocg.boot.knife.gateway.DefaultSwaggerResourcesProvider;
import in.hocg.boot.knife.gateway.SwaggerHeaderFilter;
import io.swagger.models.Swagger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@EnableConfigurationProperties(KnifeGatewayProperties.class)
@Import({BeanValidatorPluginsConfiguration.class, KnifeGatewayProperties.class})
@ConditionalOnClass({Swagger.class, EnableKnife4j.class})
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
}
