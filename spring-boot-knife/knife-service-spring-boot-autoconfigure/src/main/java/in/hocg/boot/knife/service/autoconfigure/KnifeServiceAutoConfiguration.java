package in.hocg.boot.knife.service.autoconfigure;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import in.hocg.boot.knife.service.autoconfigure.properties.KnifeServiceProperties;
import io.swagger.annotations.Api;
import io.swagger.models.Swagger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@EnableKnife4j
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
@ConditionalOnClass({Swagger.class, EnableKnife4j.class})
@EnableConfigurationProperties(KnifeServiceProperties.class)
@ConditionalOnProperty(prefix = KnifeServiceProperties.PREFIX, name = "enabled", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class KnifeServiceAutoConfiguration {

    @Value("${spring.application.name:unknown}")
    private String applicationName;
    private final KnifeServiceProperties properties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(applicationName + "服务接口文档")
            .description(properties.getDescription())
            .version(properties.getVersion())
            .contact(properties.getContact())
            .license(properties.getLicense())
            .licenseUrl(properties.getLicenseUrl())
            .build();
    }
}
