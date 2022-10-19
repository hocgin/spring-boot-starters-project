package in.hocg.boot.netty.server.autoconfiguration;

import in.hocg.boot.netty.server.autoconfiguration.bean.CommandScannerConfigurer;
import in.hocg.boot.netty.server.autoconfiguration.annotation.Command;
import in.hocg.boot.netty.server.autoconfiguration.properties.NettyServerProperties;
import in.hocg.netty.server.netty.DefaultNettyServer;
import in.hocg.netty.server.netty.NettyServer;
import in.hocg.netty.server.netty.handler.AbsForwardHandler;
import in.hocg.netty.server.netty.handler.DefaultForwardHandler;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = NettyServerProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NettyServerProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NettyServerAutoConfiguration implements InitializingBean {
    private final NettyServerProperties properties;

    @Bean
    @ConditionalOnMissingBean(NettyServer.class)
    public NettyServer nettyServer(AbsForwardHandler forwardHandler) {
        NettyServer server = new DefaultNettyServer(properties.getPort(), forwardHandler);
        server.start();
        return server;
    }

    @Bean
    @ConditionalOnMissingBean(AbsForwardHandler.class)
    public AbsForwardHandler forwardHandler() {
        return new DefaultForwardHandler();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}.Port={}", this.getClass(), properties.getPort());
    }

    public static class AutoConfiguredCommandScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        @Setter
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CommandScannerConfigurer.class);
            builder.addPropertyValue("annotationClass", Command.class);
            builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
            registry.registerBeanDefinition(CommandScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }
    }

    @Configuration(proxyBeanMethods = false)
    @Import(AutoConfiguredCommandScannerRegistrar.class)
    @ConditionalOnMissingBean({CommandScannerConfigurer.class})
    public static class CommandScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            log.debug(
                "Not found configuration for registering mapper bean using @MapperScan, MapperFactoryBean and MapperScannerConfigurer.");
        }
    }
}
